package v1.amachon.project.service.exception;

import v1.amachon.common.exception.BadRequestException;

public class ProjectApplyDeniedException extends BadRequestException {

    public ProjectApplyDeniedException() {
        super("이미 팀원이거나 지원 요청이 존재합니다.");
    }
}
