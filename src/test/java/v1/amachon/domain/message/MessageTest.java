package v1.amachon.domain.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.message.entity.Message;
import v1.amachon.domain.message.entity.MessageRoom;
import v1.amachon.domain.message.repository.MessageRepository;
import v1.amachon.domain.message.repository.MessageRoomRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MessageTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    public static Member sender;
    public static Member receiver;
    public static MessageRoom from;
    public static MessageRoom to;

    @BeforeEach
    public void init() {
        sender = Member.builder().email("sender").build();
        receiver = Member.builder().email("to").build();
        memberRepository.save(sender);
        memberRepository.save(receiver);

        from = MessageRoom.builder().from(sender).to(receiver).build();
        to = MessageRoom.builder().from(receiver).to(sender).build();
        from.match(to);
        messageRoomRepository.save(from);

    }

    @Test
    @DisplayName("메시지 생성")
    public void messageCreateTest() {
        Message message1 = Message.builder().messageRoom(from).sender(sender).receiver(receiver).content("안녕").build();
        messageRepository.save(message1);
    }

    @Test
    @DisplayName("메시지 보내기")
    public void messageSendTest() {
        Message message1 = Message.builder().messageRoom(from).sender(sender).receiver(receiver).content("안녕").build();
        Message message2 = Message.builder().messageRoom(from.getToSend()).sender(sender).receiver(receiver).content("안녕").build();
        messageRepository.save(message1);
        messageRepository.save(message2);

        assertThat(to.getReadCheck()).isEqualTo(false);
    }
}
