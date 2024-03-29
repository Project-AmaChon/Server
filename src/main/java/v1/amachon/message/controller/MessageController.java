package v1.amachon.message.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v1.amachon.message.service.dto.MessageDto;
import v1.amachon.message.service.dto.SendMessageDto;
import v1.amachon.message.service.dto.MessageRoomDto;
import v1.amachon.message.service.MessageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"메시지 API"})
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
})
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(
            value = "메시지 보내기(유저 ID 이용)",
            notes = "프로필을 통해 메시지를 보낼 때"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2010, message = "유저 아이디 값이 올바르지 않습니다.")
    })
    @PostMapping("/messages/{memberId}/send")
    public ResponseEntity<Void> sendMessageByMemberId(@RequestHeader("Authorization")String accessToken,
                                                        @PathVariable("memberId") Long memberId,
                                                        @RequestBody SendMessageDto sendMessageDto) {
        messageService.sendMessageByMemberId(memberId, sendMessageDto);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "메시지 보내기(메시지 방 ID 이용)",
            notes = "메시지 함의 메시지 방을 통해 메시지를 보낼 때"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2060, message = "대화방을 찾을 수 없습니다."),
            @ApiResponse(code = 2006, message = "해당 유저의 대화방이 아닙니다.")
    })
    @PostMapping("/messages/room/{roomId}/send")
    public ResponseEntity<Void> sendMessageByRoomId(@RequestHeader("Authorization")String accessToken,
                                                      @PathVariable("roomId") Long roomId,
                                                      @RequestBody SendMessageDto sendMessageDto) {
        messageService.sendMessageByRoomId(roomId, sendMessageDto);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "메시지 함 조회",
            notes = "메시지 함의 메시지 방 정보 리스트를 반환"
    )
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @GetMapping("/messages/room")
    public ResponseEntity<List<MessageRoomDto>> getMessageRooms(@RequestHeader("Authorization")String accessToken) {
        return ResponseEntity.ok(messageService.getMessageRooms());
    }

    @ApiOperation(
            value = "메시지 방 조회",
            notes = "메시지 방의 메시지 리스트를 반환"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2060, message = "대화방을 찾을 수 없습니다."),
            @ApiResponse(code = 2006, message = "해당 유저의 대화방이 아닙니다.")
    })
    @GetMapping("/messages/room/{roomId}")
    public ResponseEntity<List<MessageDto>> getMessageRooms(@RequestHeader("Authorization")String accessToken,
                                                          @PathVariable("roomId") Long roomId) {
        return ResponseEntity.ok(messageService.getMessages(roomId));
    }

    @ApiOperation(
            value = "메시지 조회",
            notes = "메시지 상세 정보를 반환"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2061, message = "해당 메시지를 찾을 수 없습니다."),
            @ApiResponse(code = 2006, message = "해당 유저의 메시지가 아닙니다.")
    })
    @GetMapping("/messages/room/{roomId}/{messageId}")
    public ResponseEntity<MessageDto> getMessageDetail(@RequestHeader("Authorization")String accessToken,
                                                          @PathVariable("messageId") Long messageId) {
        return ResponseEntity.ok(messageService.getMessageDetail(messageId));
    }

    @ApiOperation(
            value = "메시지 삭제",
            notes = "특정 메시지를 삭제"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2061, message = "해당 메시지를 찾을 수 없습니다."),
            @ApiResponse(code = 2006, message = "권한이 없습니다.")
    })
    @PostMapping("/messages/room/{roomId}/{messageId}/delete")
    public ResponseEntity<Void> deleteMessage(@RequestHeader("Authorization")String accessToken,
                                                     @PathVariable("messageId") Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "메시지 방 나가기",
            notes = "메시지 방과 그 방의 메시지를 모두 삭제"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2060, message = "대화방을 찾을 수 없습니다."),
            @ApiResponse(code = 2006, message = "권한이 없습니다.")
    })
    @PostMapping("/messages/room/{roomId}/leave")
    public ResponseEntity<Void> leaveMessageRoom(@RequestHeader("Authorization")String accessToken,
                                                     @PathVariable("roomId") Long roomId) {
        messageService.leaveMessageRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
