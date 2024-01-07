package v1.amachon.mail.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import v1.amachon.common.RepositoryTest;
import v1.amachon.mail.entity.EmailVerification;
import v1.amachon.mail.entity.exception.InvalidEmailFormatException;
import v1.amachon.mail.repository.EmailVerificationRepository;

@RepositoryTest
public class MailTest {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Test
    @DisplayName("이메일 형식이 올바르면 이메일 인증 정보가 정상적으로 생성된다.")
    public void successCreateEmailVerification() {
        // given
        EmailVerification emailVerification = new EmailVerification("acg6138@naver.com", "acg6138");

        // when, then
        Assertions.assertThat(emailVerification).isNotNull();
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않으면 예외가 발생한다.")
    public void failCreateEmailVerification() {
        Assertions.assertThatThrownBy(() -> new EmailVerification("invalid email format", "acg6138"))
                .isInstanceOf(InvalidEmailFormatException.class);
    }

}
