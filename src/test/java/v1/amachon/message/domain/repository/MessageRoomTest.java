package v1.amachon.message.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.common.RepositoryTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.message.entity.MessageRoom;
import v1.amachon.message.repository.MessageRepository;
import v1.amachon.message.repository.MessageRoomRepository;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
class MessageRoomTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @DisplayName("메시지방을 매칭할 수 있다")
    public void messageRoomMatchTest() {
        Member from = Member.builder().email("from").build();
        Member to = Member.builder().email("to").build();
        memberRepository.save(from);
        memberRepository.save(to);

        MessageRoom fromRoom = MessageRoom.builder().from(from).to(to).build();
        MessageRoom toRoom = MessageRoom.builder().from(to).to(from).build();
        fromRoom.match(toRoom);
        messageRoomRepository.save(fromRoom);

        assertThat(fromRoom.getToSend().getId()).isEqualTo(toRoom.getId());
        assertThat(toRoom.getToSend().getId()).isEqualTo(fromRoom.getId());
    }
}