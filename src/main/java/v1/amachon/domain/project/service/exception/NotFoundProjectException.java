package v1.amachon.domain.project.service.exception;

import v1.amachon.domain.common.exception.NotFoundException;

public class NotFoundProjectException extends NotFoundException {

    public NotFoundProjectException() {
        super("프로젝트를 찾을 수 없습니다.");
    }
}
