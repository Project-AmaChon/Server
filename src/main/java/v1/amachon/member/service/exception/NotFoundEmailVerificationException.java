package v1.amachon.member.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundEmailVerificationException extends NotFoundException {

    public NotFoundEmailVerificationException() {
        super("이메일 인증 정보를 찾을 수 없습니다.");
    }
}
