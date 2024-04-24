package v1.amachon.common.config.jwt.exception;


public class ExpiredTokenException extends JwtException {

    public ExpiredTokenException() {
        super("토큰이 만료되었습니다.");
    }
}
