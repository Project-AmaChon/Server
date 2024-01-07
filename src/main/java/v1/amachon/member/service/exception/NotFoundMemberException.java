package v1.amachon.member.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundMemberException extends NotFoundException {

    public NotFoundMemberException() {
        super("유저를 찾을 수 없습니다.");
    }
}
