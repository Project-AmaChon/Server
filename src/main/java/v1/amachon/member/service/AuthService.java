package v1.amachon.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.common.config.jwt.JwtTokenUtil;
import v1.amachon.common.config.jwt.constants.JwtExpiration;
import v1.amachon.common.config.jwt.constants.JwtHeaderUtil;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.mail.service.dto.CertificationDto;
import v1.amachon.mail.entity.EmailVerification;
import v1.amachon.mail.repository.EmailVerificationRepository;
import v1.amachon.member.service.dto.login.LoginDto;
import v1.amachon.member.service.dto.login.TokenDto;
import v1.amachon.member.entity.Member;
import v1.amachon.member.entity.auth.RefreshToken;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.member.repository.auth.RefreshTokenRedisRepository;
import v1.amachon.member.service.exception.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public void certify(CertificationDto certificationDto) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(certificationDto.getEmail())
                .orElseThrow(NotFoundEmailVerificationException::new);
        if (!emailVerification.getExpiredDate().isAfter(LocalDateTime.now()) ||
                !emailVerification.getCode().equals(certificationDto.getCode())) {
            throw new ExpiredEmailVerificationException();
        }
    }

    public TokenDto login(LoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(NotFoundMemberException::new);
        if (member.getStatus().equals(BaseEntity.Status.EXPIRED)) {
            throw new ExpiredMemberException();
        }
        checkPassword(loginDto.getPassword(), member.getPassword());
        String accessToken = jwtTokenUtil.generateAccessToken(member.getEmail());
        RefreshToken refreshToken = saveRefreshToken(member.getEmail());
        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken.getRefreshToken())
                .grantType(JwtHeaderUtil.GRANT_TYPE.getValue()).memberId(member.getId()).build();
    }

    public void checkPassword(String rawPassword, String findMemberPassword) {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new InvalidPasswordException();
        }
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
                jwtTokenUtil.generateRefreshToken(username), JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
    }
}
