package v1.amachon.message.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundMessageRoomException extends NotFoundException {

    public NotFoundMessageRoomException() {
        super("메시지 방이 존재하지 않습니다.");
    }
}
