package v1.amachon.message.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import v1.amachon.common.IntegrationTest;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.member.service.exception.NotFoundMemberException;
import v1.amachon.message.entity.Message;
import v1.amachon.message.entity.MessageRoom;
import v1.amachon.message.repository.MessageRepository;
import v1.amachon.message.repository.MessageRoomRepository;
import v1.amachon.message.service.dto.MessageDto;
import v1.amachon.message.service.dto.MessageRoomDto;
import v1.amachon.message.service.dto.SendMessageRequest;
import v1.amachon.message.service.exception.NotFoundMessageRoomException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static v1.amachon.common.entity.BaseEntity.Status.EXPIRED;
import static v1.amachon.fixtures.MemberFixtures.*;

public class MessageServiceTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

    @Autowired
    private MessageService messageService;

    @BeforeEach
    public void setUp() {
        Member from = MemberFixtures.정우();
        Member to = MemberFixtures.종범();

        memberRepository.save(from);
        memberRepository.save(to);
        memberRepository.save(MemberFixtures.규범());
        memberRepository.save(MemberFixtures.재욱());
        memberRepository.save(MemberFixtures.승현());

        MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
        MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
        fromMessageRoom.match(toMessageRoom);
        messageRoomRepository.save(fromMessageRoom);
    }

    @DisplayName("유저 ID 기반 메시지 전송")
    @Nested
    class sendMessageByMemberId {

        @WithMockUser(value = 정우_이메일)
        @Test
        @DisplayName("유저 ID를 통해 메시지를 보낼 수 있다")
        void sendMessage() {
            // given
            Member from = memberRepository.findByEmail(정우_이메일).get();
            Member to = memberRepository.findByEmail(승현_이메일).get();
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when
            messageService.sendMessageByMemberId(to.getId(), request);
            MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
            List<Message> messages = messageRepository.findByMessageRoomId(messageRoom.getId());

            // then
            assertThat(messages.size()).isEqualTo(1);
        }

        @WithMockUser(value = 정우_이메일)
        @Test
        @DisplayName("해당 유저에게 처음 보내는 메시지인 경우 메시지 방이 생성된다")
        void createdMessageRoom() {
            // given
            Member from = memberRepository.findByEmail(정우_이메일).get();
            Member to = memberRepository.findByEmail(승현_이메일).get();
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when
            messageService.sendMessageByMemberId(to.getId(), request);
            MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();

            // then
            assertThat(messageRoom).isNotNull();
        }

        @WithMockUser(value = 정우_이메일)
        @Test
        @DisplayName("해당 유저에게 보내는 메시지가 처음이 아닌 경우 생성되어 있는 방을 재사용한다")
        void reUseMessageRoom() {
            // given
            Member from = memberRepository.findByEmail(정우_이메일).get();
            Member to = memberRepository.findByEmail(종범_이메일).get();
            MessageRoom expected = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when
            messageService.sendMessageByMemberId(to.getId(), request);
            MessageRoom actual = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();

            // then
            assertThat(expected).isEqualTo(actual);
        }

        @WithMockUser(value = 정우_이메일)
        @Test
        @DisplayName("존재하지 않는 유저에게 메시지를 보내면 404 에러가 발생한다")
        void notFoundMember() {
            // given
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when, then
            Assertions.assertThatThrownBy(() -> messageService.sendMessageByMemberId(-1L, request))
                    .isInstanceOf(NotFoundMemberException.class)
                    .hasMessage("유저를 찾을 수 없습니다.");
        }

        @Test
        @WithAnonymousUser
        @DisplayName("로그인 되어있지 않은 유저가 유저 ID를 통해 메시지를 보내면 401 에러가 발생한다")
        void sendFailWithAnonymousUser() {
            // given
            Member to = memberRepository.findByEmail(승현_이메일).get();
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when, then
            Assertions.assertThatThrownBy(() -> messageService.sendMessageByMemberId(to.getId(), request))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("로그인이 필요합니다.");
        }
    }

    @DisplayName("메시지 방 ID 기반 메시지 전송")
    @Nested
    class sendMessageByMessageRoomId {

        @WithMockUser(value = 정우_이메일)
        @Test
        @DisplayName("메시지 방 ID를 통해 메시지를 보낼 수 있다")
        void sendMessage() {
            // given
            Member from = memberRepository.findByEmail(정우_이메일).get();
            Member to = memberRepository.findByEmail(종범_이메일).get();
            MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when
            messageService.sendMessageByRoomId(messageRoom.getId(), request);
            List<Message> messages = messageRepository.findByMessageRoomId(messageRoom.getId());

            // then
            assertThat(messages.size()).isEqualTo(1);
        }

        @WithMockUser(value = 정우_이메일)
        @Test
        @DisplayName("존재하지 않는 메시지 방에 메시지를 보내는 경우 404 에러가 발생한다")
        void notFoundMessageRoom() {
            // given
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when, then
            Assertions.assertThatThrownBy(() -> messageService.sendMessageByRoomId(-1L, request))
                    .isInstanceOf(NotFoundMessageRoomException.class)
                    .hasMessage("메시지 방이 존재하지 않습니다.");
        }

        @Test
        @WithAnonymousUser
        @DisplayName("로그인 되어있지 않은 유저가 메시지 방 ID를 통해 메시지를 보내면 401 에러가 발생한다")
        void sendFailWithAnonymousUser() {
            // given
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

            // when, then
            Assertions.assertThatThrownBy(() -> messageService.sendMessageByRoomId(-1L, request))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("로그인이 필요합니다.");
        }

        @WithMockUser(value = 정우_이메일)
        @Test
        @DisplayName("자신의 메시지 방이 아닌 메시지 방에 메시지를 보내면 400 에러가 발생한다")
        void sendMessageWithOthersMessageRoomId() {
            // given
            Member from = memberRepository.findByEmail(재욱_이메일).get();
            Member to = memberRepository.findByEmail(규범_이메일).get();
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
            MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
            MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
            fromMessageRoom.match(toMessageRoom);
            messageRoomRepository.save(fromMessageRoom);

            // when, then
            Assertions.assertThatThrownBy(() -> messageService.sendMessageByRoomId(fromMessageRoom.getId(), request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("올바르지 않은 요청입니다.");
        }
    }

    @DisplayName("메시지 방 목록을 가져올 수 있다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void getMessageRooms() {
        // when
        List<MessageRoomDto> messageRooms = messageService.getMessageRooms();

        // then
        assertThat(messageRooms.size()).isEqualTo(1);
    }

    @DisplayName("읽지 않은 메시지의 개수를 가져올 수 있다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void getUnReadMessageCount() {
        // when
        Member from = memberRepository.findByEmail(정우_이메일).get();
        Member to = memberRepository.findByEmail(종범_이메일).get();
        MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        messageService.sendMessageByRoomId(messageRoom.getId(), request);

        // then
        assertThat(messageRoom.getToSend().getUnReadMessageCount()).isEqualTo(1);
    }

    @DisplayName("메시지 목록을 가져올 수 있다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void getMessages() {
        // given
        Member from = memberRepository.findByEmail(정우_이메일).get();
        Member to = memberRepository.findByEmail(종범_이메일).get();
        MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        messageService.sendMessageByRoomId(messageRoom.getId(), request);

        // when
        List<MessageDto> messages = messageService.getMessages(messageRoom.getId());

        // then
        assertThat(messages.size()).isEqualTo(1);
    }

    @DisplayName("자신의 메시지 방이 아닌 메시지 방의 메시지 목록을 가져오는 경우 400 에러가 발생한다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void getMessagesWithOthersMessageRoomId() {
        // given
        Member from = memberRepository.findByEmail(재욱_이메일).get();
        Member to = memberRepository.findByEmail(규범_이메일).get();
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
        MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
        fromMessageRoom.match(toMessageRoom);
        messageRoomRepository.save(fromMessageRoom);

        // when, then
        Assertions.assertThatThrownBy(() -> messageService.getMessages(fromMessageRoom.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("올바르지 않은 요청입니다.");
    }

    @DisplayName("상세 메시지를 조회할 수 있다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void getMessageDetail() {
        // given
        Member from = memberRepository.findByEmail(정우_이메일).get();
        Member to = memberRepository.findByEmail(종범_이메일).get();
        MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        messageService.sendMessageByRoomId(messageRoom.getId(), request);
        Message message = messageRoom.getMessages().get(0);

        // when
        MessageDto messageDetail = messageService.getMessageDetail(message.getId());

        // then
        assertThat(messageDetail)
                .extracting("memberId", "profileUrl", "nickname", "content")
                .containsExactly(
                        from.getId(), from.getProfile().getProfileImageUrl(), from.getNickname(), request.getContent()
                );
    }

    @DisplayName("자신의 메시지가 아닌 상세 메시지를 조회하는 경우 400 에러가 발생한다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void getMessageDetailWithOthers() {
        // given
        Member from = memberRepository.findByEmail(재욱_이메일).get();
        Member to = memberRepository.findByEmail(규범_이메일).get();
        MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
        MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
        fromMessageRoom.match(toMessageRoom);
        messageRoomRepository.save(fromMessageRoom);

        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        messageService.send(fromMessageRoom, request);

        MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
        Message message = messageRoom.getMessages().get(0);

        // when, then
        Assertions.assertThatThrownBy(() -> messageService.getMessageDetail(message.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("올바르지 않은 요청입니다.");
    }

    @DisplayName("메시지를 삭제할 수 있다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void deleteMessage() {
        // given
        Member from = memberRepository.findByEmail(정우_이메일).get();
        Member to = memberRepository.findByEmail(종범_이메일).get();
        MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        messageService.sendMessageByRoomId(messageRoom.getId(), request);
        Message message = messageRoom.getMessages().get(0);

        // when
        messageService.deleteMessage(message.getId());

        // then
        assertThat(message.getStatus()).isEqualTo(EXPIRED);
    }


    @DisplayName("자신의 메시지가 아닌 메시지를 삭제하는 경우 400 에러가 발생한다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void deleteMessageDetailWithOthers() {
        // given
        Member from = memberRepository.findByEmail(재욱_이메일).get();
        Member to = memberRepository.findByEmail(규범_이메일).get();
        MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
        MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
        fromMessageRoom.match(toMessageRoom);
        messageRoomRepository.save(fromMessageRoom);

        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        messageService.send(fromMessageRoom, request);

        MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
        Message message = messageRoom.getMessages().get(0);

        // when, then
        Assertions.assertThatThrownBy(() -> messageService.deleteMessage(message.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("올바르지 않은 요청입니다.");
    }

    @DisplayName("메시지 방을 나가는 경우 모든 메시지와 메시지 방의 상태는 EXPIRED로 변경된다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void leaveMessageRoom() {
        // given
        Member from = memberRepository.findByEmail(정우_이메일).get();
        Member to = memberRepository.findByEmail(종범_이메일).get();
        MessageRoom messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId()).get();
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        messageService.sendMessageByRoomId(messageRoom.getId(), request);
        messageService.sendMessageByRoomId(messageRoom.getId(), request);
        List<Message> messages = messageRepository.findByMessageRoomId(messageRoom.getId());

        // when
        messageService.leaveMessageRoom(messageRoom.getId());

        // then
        assertAll(
                () -> assertThat(messageRoom.getStatus()).isEqualTo(EXPIRED),
                () -> assertThat(messages.get(0).getStatus()).isEqualTo(EXPIRED),
                () -> assertThat(messages.get(1).getStatus()).isEqualTo(EXPIRED)
        );
    }

    @DisplayName("자신의 메시지 방이 아닌 메시지 방을 나가는 경우 400 에러가 발생한다")
    @WithMockUser(value = 정우_이메일)
    @Test
    void leaveMessageRoomWithOthers() {
        // given
        Member from = memberRepository.findByEmail(재욱_이메일).get();
        Member to = memberRepository.findByEmail(규범_이메일).get();
        MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
        MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
        fromMessageRoom.match(toMessageRoom);
        messageRoomRepository.save(fromMessageRoom);

        // when, then
        Assertions.assertThatThrownBy(() -> messageService.leaveMessageRoom(fromMessageRoom.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("올바르지 않은 요청입니다.");
    }


}
