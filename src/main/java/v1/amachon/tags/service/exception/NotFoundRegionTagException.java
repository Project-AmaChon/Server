package v1.amachon.tags.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundRegionTagException extends NotFoundException {
    public NotFoundRegionTagException() {
        super("지역 태그를 찾을 수 없습니다.");
    }
}
