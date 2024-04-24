package v1.amachon.common.config.jwt.exception;


import v1.amachon.common.exception.UnauthorizedException;

public class InvalidTokenFormatException extends JwtException {

    public InvalidTokenFormatException() {
        super("토큰 형식이 올바르지 않습니다.");
    }
}
