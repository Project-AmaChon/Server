package v1.amachon.common.config.jwt.exception;


public class InvalidTokenException extends JwtException {
    public InvalidTokenException() {
        super("토큰과 username이 일치하지 않습니다.");
    }
}
