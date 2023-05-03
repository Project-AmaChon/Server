package v1.amachon.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.mail.dto.CertificationDto;
import v1.amachon.domain.mail.service.EmailService;
import v1.amachon.domain.member.service.AuthService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/send/verification-code")
    public void sendVerificationCode(@RequestBody Map<String, String> data) throws MessagingException, UnsupportedEncodingException {
        emailService.sendVerificationCode(data.get("email"));
    }

    @PostMapping("/certification")
    public boolean certification(@RequestBody CertificationDto certificationDto) {
        return authService.certify(certificationDto);
    }
}
