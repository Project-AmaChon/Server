package v1.amachon.domain.message.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.message.dto.MessageDto;
import v1.amachon.domain.message.dto.SendMessageDto;
import v1.amachon.domain.message.dto.MessageRoomDto;
import v1.amachon.domain.message.service.MessageService;

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
    public BaseResponse<String> sendMessageByMemberId(@RequestHeader("Authorization")String accessToken,
                                                      @PathVariable("memberId") Long memberId,
                                                      @RequestBody SendMessageDto sendMessageDto) {
        try {
            messageService.sendMessageByMemberId(memberId, sendMessageDto);
            return new BaseResponse<>("메시지 보내기 성공!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
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
    public BaseResponse<String> sendMessageByRoomId(@RequestHeader("Authorization")String accessToken,
                                                      @PathVariable("roomId") Long roomId,
                                                      @RequestBody SendMessageDto sendMessageDto) {
        try {
            messageService.sendMessageByRoomId(roomId, sendMessageDto);
            return new BaseResponse<>("메시지 보내기 성공!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "메시지 함 조회",
            notes = "메시지 함의 메시지 방 정보 리스트를 반환"
    )
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @GetMapping("/messages/room")
    public BaseResponse<List<MessageRoomDto>> getMessageRooms(@RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(messageService.getMessageRooms());
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
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
    public BaseResponse<List<MessageDto>> getMessageRooms(@RequestHeader("Authorization")String accessToken,
                                                          @PathVariable("roomId") Long roomId) {
        try {
            return new BaseResponse<>(messageService.getMessages(roomId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
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
    public BaseResponse<MessageDto> getMessageDetail(@RequestHeader("Authorization")String accessToken,
                                                          @PathVariable("messageId") Long messageId) {
        try {
            return new BaseResponse<>(messageService.getMessageDetail(messageId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
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
    public BaseResponse<String> deleteMessage(@RequestHeader("Authorization")String accessToken,
                                                     @PathVariable("messageId") Long messageId) {
        try {
            messageService.deleteMessage(messageId);
            return new BaseResponse<>("메세지 삭제 성공!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
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
    public BaseResponse<String> leaveMessageRoom(@RequestHeader("Authorization")String accessToken,
                                                     @PathVariable("roomId") Long roomId) {
        try {
            messageService.leaveMessageRoom(roomId);
            return new BaseResponse<>("메세지 방 나가기 성공!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
