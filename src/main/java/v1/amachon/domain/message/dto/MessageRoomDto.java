package v1.amachon.domain.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.domain.message.entity.MessageRoom;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRoomDto {

    private Long roomId;
    private int hoursAgo;
    private int unReadMessageCount;
    private Long memberId;
    private String profileUrl;
    private String nickname;
    private String lastMessage;

    public MessageRoomDto(MessageRoom messageRoom) {
        this.roomId = messageRoom.getId();
        this.hoursAgo = (int) ChronoUnit.HOURS.between(messageRoom.getLastModifiedDate(), LocalDateTime.now());
        this.unReadMessageCount = messageRoom.getUnReadMessageCount();
        this.memberId = messageRoom.getTo().getId();
        this.profileUrl = messageRoom.getTo().getProfile().getProfileImageUrl();
        this.nickname = messageRoom.getTo().getNickname();
        this.lastMessage = messageRoom.getLastMessage();
    }
}
