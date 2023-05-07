package v1.amachon.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.mail.dto.CertificationDto;
import v1.amachon.domain.mail.entity.EmailVerification;
import v1.amachon.domain.mail.repository.EmailVerificationRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final EmailVerificationRepository emailVerificationRepository;

    public void certify(CertificationDto certificationDto) throws BaseException {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(certificationDto.getEmail()).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BAD_REQUEST));
        if (emailVerification.getExpiredDate().isAfter(LocalDateTime.now()) &&
                emailVerification.getCode().equals(certificationDto.getCode())) {
            return;
        }
        throw new BaseException(BaseResponseStatus.EXPIRED_CODE);
    }
}
