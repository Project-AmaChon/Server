package v1.amachon.common.advice;

public class ErrorResponse {

    private final String message;
    private int code;

    public ErrorResponse(final String message, final int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}