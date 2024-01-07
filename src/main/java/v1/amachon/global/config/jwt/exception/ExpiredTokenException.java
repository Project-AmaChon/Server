package v1.amachon.global.config.jwt.exception;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException() {
        super("토큰이 만료되었습니다.");
    }
}
