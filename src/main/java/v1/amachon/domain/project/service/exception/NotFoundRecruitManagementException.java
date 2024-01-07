package v1.amachon.domain.project.service.exception;

import v1.amachon.domain.common.exception.NotFoundException;

public class NotFoundRecruitManagementException extends NotFoundException {

    public NotFoundRecruitManagementException() {
        super("지원 요청을 찾을 수 없습니다.");
    }
}
