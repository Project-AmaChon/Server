package v1.amachon.project.service.exception;

import v1.amachon.common.exception.NotFoundException;

public class NotFoundTeamMemberException extends NotFoundException {

    public NotFoundTeamMemberException() {
        super("팀원을 찾을 수 없습니다.");
    }
}
