package v1.amachon.member.service.exception;

public class ExpiredMemberException extends RuntimeException {

    public ExpiredMemberException() {
        super("탈퇴한 회원입니다.");
    }
}
