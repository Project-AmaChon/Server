package v1.amachon.message.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v1.amachon.common.config.security.AuthenticatedMemberEmail;
import v1.amachon.message.service.dto.MessageDto;
import v1.amachon.message.service.dto.SendMessageRequest;
import v1.amachon.message.service.dto.MessageRoomDto;
import v1.amachon.message.service.MessageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"메시지 API"})
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "메시지 보내기(유저 ID 이용)", notes = "프로필을 통해 메시지를 보낼 때")
    @PostMapping("/messages/{memberId}/send")
    public ResponseEntity<Void> sendMessageByMemberId(@AuthenticatedMemberEmail String email, @PathVariable("memberId") Long memberId,
                                                      @RequestBody SendMessageRequest sendMessageRequest) {
        messageService.sendMessageByMemberId(memberId, sendMessageRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "메시지 보내기(메시지 방 ID 이용)", notes = "메시지 함의 메시지 방을 통해 메시지를 보낼 때")
    @PostMapping("/messages/room/{roomId}/send")
    public ResponseEntity<Void> sendMessageByRoomId(@RequestHeader("Authorization")String accessToken,
                                                      @PathVariable("roomId") Long roomId,
                                                      @RequestBody SendMessageRequest sendMessageRequest) {
        messageService.sendMessageByRoomId(roomId, sendMessageRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "메시지 함 조회", notes = "메시지 함의 메시지 방 정보 리스트를 반환")
    @GetMapping("/messages/room")
    public ResponseEntity<List<MessageRoomDto>> getMessageRooms(@RequestHeader("Authorization")String accessToken) {
        return ResponseEntity.ok(messageService.getMessageRooms());
    }

    @ApiOperation(value = "메시지 방 조회", notes = "메시지 방의 메시지 리스트를 반환")
    @GetMapping("/messages/room/{roomId}")
    public ResponseEntity<List<MessageDto>> getMessageRooms(@RequestHeader("Authorization")String accessToken,
                                                            @PathVariable("roomId") Long roomId) {
        return ResponseEntity.ok(messageService.getMessages(roomId));
    }

    @ApiOperation(value = "메시지 조회", notes = "메시지 상세 정보를 반환")
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<MessageDto> getMessageDetail(@RequestHeader("Authorization")String accessToken,
                                                       @PathVariable("messageId") Long messageId) {
        return ResponseEntity.ok(messageService.getMessageDetail(messageId));
    }

    @ApiOperation(value = "메시지 삭제", notes = "특정 메시지를 삭제")
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@RequestHeader("Authorization")String accessToken,
                                                     @PathVariable("messageId") Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "메시지 방 나가기", notes = "메시지 방과 그 방의 메시지를 모두 삭제")
    @PostMapping("/messages/room/{roomId}/leave")
    public ResponseEntity<Void> leaveMessageRoom(@RequestHeader("Authorization")String accessToken,
                                                     @PathVariable("roomId") Long roomId) {
        messageService.leaveMessageRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
