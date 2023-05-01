package v1.amachon.domain.message.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.domain.base.BaseEntity;
import v1.amachon.domain.member.entity.Member;

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

    private Boolean readCheck;

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
        this.readCheck = true;
        this.from = from;
        this.to = to;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        this.toSend.readCheck = false;
    }

    public void match(MessageRoom toSend) {
        this.toSend = toSend;
        toSend.toSend = this;
    }
}
