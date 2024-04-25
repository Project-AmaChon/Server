package v1.amachon.message.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.message.entity.Message;
import v1.amachon.message.entity.MessageRoom;
import v1.amachon.message.repository.MessageRepository;
import v1.amachon.message.repository.MessageRoomRepository;

import static v1.amachon.fixtures.MemberFixtures.정우_이메일;
import static v1.amachon.fixtures.MemberFixtures.종범_이메일;

@SpringBootTest
class MessageTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        Member sender = Member.builder().email(정우_이메일).build();
        Member receiver = Member.builder().email(종범_이메일).build();
        memberRepository.save(sender);
        memberRepository.save(receiver);

        MessageRoom from = MessageRoom.builder().from(sender).to(receiver).build();
        MessageRoom to = MessageRoom.builder().from(receiver).to(sender).build();
        from.match(to);
        messageRoomRepository.save(from);
    }

    @Test
    @DisplayName("메시지를 생성할 수 있다.")
    public void messageCreateTest() {
        // given
        Member sender = memberRepository.findByEmail(정우_이메일).get();
        Member receiver = memberRepository.findByEmail(종범_이메일).get();
        MessageRoom from = messageRoomRepository.findByFromAndTo(sender.getId(), receiver.getId()).get();

        // when
        Message message = Message.builder().messageRoom(from).sender(sender).receiver(receiver).content("안녕").build();
        messageRepository.saveAndFlush(message);

        Message findMessage = messageRepository.findById(message.getId()).get();

        // then
        Assertions.assertThat(findMessage).isNotNull();
    }
}
