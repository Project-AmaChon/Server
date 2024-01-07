package v1.amachon.domain.member.service.exception;

public class ExpiredEmailVerificationException extends RuntimeException {

    public ExpiredEmailVerificationException() {
        super("이메일 인증 정보가 만료되었습니다.");
    }
}
