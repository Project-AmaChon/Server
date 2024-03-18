package v1.amachon.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import v1.amachon.mail.entity.EmailVerification;
import v1.amachon.mail.repository.EmailVerificationRepository;
import v1.amachon.mail.service.exception.FailureSendMailException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public boolean sendVerificationCode(String to) {
        MimeMessage message = javaMailSender.createMimeMessage();

        String ePw = createKey();
        String subject = "아마촌 회원 가입 인증 코드가 발송되었습니다.";
        String content = "<div style='margin:20px;'>"
                + "<h1>인증번호 메일입니다.</h1>"
                + "<br>"
                + "<p>아래 코드를 복사해 입력해주세요<p>"
                + "<br>"
                + "<p>감사합니다.<p>"
                + "<br>"
                + "<div align='center' style='border:1px solid black; font-family:verdana';>"
                + "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>"
                + "<div style='font-size:130%'>"
                + "CODE : <strong>" + ePw + "</strong><div><br/> "
                + "</div>";

        try {
            message.setFrom(sender);
            message.setRecipient(MimeMessage.RecipientType.TO, new javax.mail.internet.InternetAddress(to));
            message.setSubject(subject, "UTF-8");
            message.setContent(content, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new FailureSendMailException();
        }
        javaMailSender.send(message);
        emailVerificationRepository.save(new EmailVerification(to, ePw));
        return true;
    }

    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);
            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }
}
