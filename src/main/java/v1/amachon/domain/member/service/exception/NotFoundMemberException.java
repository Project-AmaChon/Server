package v1.amachon.domain.member.service.exception;

import v1.amachon.domain.common.exception.NotFoundException;

public class NotFoundMemberException extends NotFoundException {

    public NotFoundMemberException() {
        super("유저를 찾을 수 없습니다.");
    }
}
