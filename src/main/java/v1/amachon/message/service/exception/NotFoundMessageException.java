package v1.amachon.message.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundMessageException extends NotFoundException {

    public NotFoundMessageException() {
        super("메시지가 존재하지 않습니다.");
    }
}
