package v1.amachon.domain.mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.mail.entity.EmailVerification;
import v1.amachon.domain.mail.repository.EmailVerificationRepository;
import v1.amachon.domain.mail.service.EmailService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@SpringBootTest
public class MailTest {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private EmailService emailService;

    @Test
    public void createEmailVerification() {
        EmailVerification emailVerification = new EmailVerification("acg6138@naver.com", "acg6138");
        emailVerificationRepository.save(emailVerification);
        System.out.println("emailVerification.getExpiredDate() = " + emailVerification.getExpiredDate());
    }

    @Test
    public void sendVerificationCode() throws MessagingException, UnsupportedEncodingException {
        String email = "acg6138@naver.com";
        emailService.sendVerificationCode(email);
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("없는 이메일"));
        System.out.println("emailVerification.getCode() = " + emailVerification.getCode());
        System.out.println("emailVerification.getExpiredDate() = " + emailVerification.getExpiredDate());
    }
}
