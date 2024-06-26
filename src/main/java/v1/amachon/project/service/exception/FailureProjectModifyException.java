package v1.amachon.project.service.exception;

import v1.amachon.common.exception.UnauthorizedException;

public class FailureProjectModifyException extends UnauthorizedException {

    public FailureProjectModifyException() {
        super("프로젝트를 수정하거나 삭제할 권한이 없습니다.");
    }
}
