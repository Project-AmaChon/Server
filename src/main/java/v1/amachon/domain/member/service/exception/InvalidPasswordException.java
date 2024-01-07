package v1.amachon.domain.member.service.exception;

import v1.amachon.domain.common.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {

    public InvalidPasswordException() {
        super("비밀번호가 올바르지 않습니다.");
    }
}
