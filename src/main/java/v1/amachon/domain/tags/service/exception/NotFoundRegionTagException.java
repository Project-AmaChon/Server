package v1.amachon.domain.tags.service.exception;

import v1.amachon.domain.common.exception.NotFoundException;

public class NotFoundRegionTagException extends NotFoundException {
    public NotFoundRegionTagException() {
        super("지역 태그를 찾을 수 없습니다.");
    }
}
