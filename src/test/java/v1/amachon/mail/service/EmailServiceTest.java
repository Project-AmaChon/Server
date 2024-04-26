package v1.amachon.mail.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import v1.amachon.mail.entity.EmailVerification;
import v1.amachon.mail.repository.EmailVerificationRepository;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Spy
    @InjectMocks
    private EmailService emailService;

    @Test
    @DisplayName("인증 이메일을 전송할 수 있다.")
    public void testSendVerificationCode() throws MessagingException {
        // given
        MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
        message.setFrom("test@test.com");
        String key = "testKey";
        String to = "acg6138@naver.com";

        when(javaMailSender.createMimeMessage()).thenReturn(message);
        doReturn(key).when(emailService).createKey();
        doNothing().when(javaMailSender).send(message);

        // when
        emailService.sendVerificationCode(to);

        // then
        verify(javaMailSender, times(1)).send(message);
        verify(emailVerificationRepository, times(1)).save(any(EmailVerification.class));
    }
}
