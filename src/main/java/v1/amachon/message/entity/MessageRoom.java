package v1.amachon.message.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.member.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRoom extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "message_room_id")
    private Long id;

    private String lastMessage;
    private int unReadMessageCount;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private MessageRoom toSend;

    @OneToMany(mappedBy = "messageRoom", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member to;

    @Builder
    public MessageRoom(Member from, Member to) {
        this.from = from;
        this.to = to;
    }

    public void sendMessage(Message fromMessage, Message toMessage) {
        this.unReadMessageCount = 0;
        this.lastMessage = fromMessage.getContent();
        this.messages.add(fromMessage);
        this.toSend.messages.add(toMessage);
        this.toSend.unReadMessageCount += 1;
        this.toSend.lastMessage = toMessage.getContent();
    }

    public void match(MessageRoom toSend) {
        this.toSend = toSend;
        toSend.toSend = this;
    }

    public void initUnReadMessageCount() {
        this.unReadMessageCount = 0;
    }
}
