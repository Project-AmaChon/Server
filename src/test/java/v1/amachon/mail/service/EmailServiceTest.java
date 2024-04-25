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
        // Given
        MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
        message.setFrom("test@test.com");

        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        when(javaMailSender.createMimeMessage()).thenReturn(message);
        doReturn(any(String.class)).when(emailService).createKey();

        // When
        emailService.sendVerificationCode(any(String.class));

        // Then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(emailVerificationRepository, times(1)).save(any(EmailVerification.class));
    }
}
