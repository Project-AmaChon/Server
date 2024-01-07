package v1.amachon.common.config.jwt.exception;

import v1.amachon.common.exception.InvalidFormatException;

public class InvalidTokenFormatException extends InvalidFormatException {

    public InvalidTokenFormatException() {
        super("토큰 형식이 올바르지 않습니다.");
    }
}
