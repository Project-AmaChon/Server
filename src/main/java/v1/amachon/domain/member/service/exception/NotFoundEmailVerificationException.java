package v1.amachon.domain.member.service.exception;

import v1.amachon.domain.common.exception.NotFoundException;

public class NotFoundEmailVerificationException extends NotFoundException {

    public NotFoundEmailVerificationException() {
        super("이메일 인증 정보를 찾을 수 없습니다.");
    }
}
