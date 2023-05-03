package v1.amachon.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.mail.dto.CertificationDto;
import v1.amachon.domain.mail.entity.EmailVerification;
import v1.amachon.domain.mail.repository.EmailVerificationRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final EmailVerificationRepository emailVerificationRepository;

    public boolean certify(CertificationDto certificationDto) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(certificationDto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("인증 정보가 존재하지 않습니다."));
        if (emailVerification.getExpiredDate().isAfter(LocalDateTime.now()) &&
                emailVerification.getCode().equals(certificationDto.getCode())) {
            return true;
        }
        return false;
    }
}
