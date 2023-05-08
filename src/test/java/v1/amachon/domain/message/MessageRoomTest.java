package v1.amachon.domain.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.message.entity.MessageRoom;
import v1.amachon.domain.message.repository.MessageRepository;
import v1.amachon.domain.message.repository.MessageRoomRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MessageRoomTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Test
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