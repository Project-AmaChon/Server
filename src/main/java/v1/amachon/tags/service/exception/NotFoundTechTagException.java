package v1.amachon.tags.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundTechTagException extends NotFoundException {

    public NotFoundTechTagException() {
        super("기술 태그를 찾을 수 없습니다.");
    }
}
