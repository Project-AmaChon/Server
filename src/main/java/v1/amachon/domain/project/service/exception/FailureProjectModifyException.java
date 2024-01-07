package v1.amachon.domain.project.service.exception;

import v1.amachon.domain.common.exception.UnauthorizedException;

public class FailureProjectModifyException extends UnauthorizedException {

    public FailureProjectModifyException() {
        super("작성자만 프로젝트 글을 수정할 수 있습니다.");
    }
}
