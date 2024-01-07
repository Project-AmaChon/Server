package v1.amachon.domain.member.service.exception;

public class DuplicatedNicknameException extends RuntimeException{

    public DuplicatedNicknameException() {
        super("중복되는 닉네임이 존재합니다.");
    }
}
