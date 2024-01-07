package v1.amachon.domain.mail.service.exception;

public class FailureSendMailException extends RuntimeException {

    public FailureSendMailException() {
        super("이메일 전송에서 문제가 발생했습니다.");
    }
}
