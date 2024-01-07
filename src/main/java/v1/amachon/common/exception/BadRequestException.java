package v1.amachon.common.exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super("올바르지 않은 요청입니다.");
    }
}
