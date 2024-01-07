package v1.amachon.global.config.jwt.exception;

import v1.amachon.domain.common.exception.InvalidFormatException;

public class InvalidTokenFormatException extends InvalidFormatException {

    public InvalidTokenFormatException() {
        super("토큰 형식이 올바르지 않습니다.");
    }
}
