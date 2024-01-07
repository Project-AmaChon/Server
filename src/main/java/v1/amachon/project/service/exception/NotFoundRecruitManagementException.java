package v1.amachon.project.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundRecruitManagementException extends NotFoundException {

    public NotFoundRecruitManagementException() {
        super("지원 요청을 찾을 수 없습니다.");
    }
}
