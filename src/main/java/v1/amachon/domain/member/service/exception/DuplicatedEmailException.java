package v1.amachon.domain.member.service.exception;

public class DuplicatedEmailException extends RuntimeException{

    public DuplicatedEmailException() {
        super("중복되는 이메일이 존재합니다.");
    }
}
