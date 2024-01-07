package v1.amachon.domain.mail.entity.exception;

import v1.amachon.domain.common.exception.InvalidFormatException;

public class InvalidEmailFormatException extends InvalidFormatException {

    public InvalidEmailFormatException() {
        super("이메일 형식이 올바르지 않습니다.");
    }
}
