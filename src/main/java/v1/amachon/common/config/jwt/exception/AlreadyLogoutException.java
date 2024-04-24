package v1.amachon.common.config.jwt.exception;


public class AlreadyLogoutException extends JwtException {
    public AlreadyLogoutException() {
        super("이미 로그아웃 된 회원입니다.");
    }
}
