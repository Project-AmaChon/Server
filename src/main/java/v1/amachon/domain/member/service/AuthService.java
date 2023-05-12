package v1.amachon.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseEntity;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.mail.dto.CertificationDto;
import v1.amachon.domain.mail.entity.EmailVerification;
import v1.amachon.domain.mail.repository.EmailVerificationRepository;
import v1.amachon.domain.member.dto.login.LoginDto;
import v1.amachon.domain.member.dto.login.TokenDto;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.entity.auth.RefreshToken;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.member.repository.auth.LogoutAccessTokenRedisRepository;
import v1.amachon.domain.member.repository.auth.RefreshTokenRedisRepository;
import v1.amachon.global.config.jwt.JwtTokenUtil;
import v1.amachon.global.config.jwt.constants.JwtHeaderUtil;

import java.time.LocalDateTime;

import static v1.amachon.global.config.jwt.constants.JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public void certify(CertificationDto certificationDto) throws BaseException {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(certificationDto.getEmail()).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BAD_REQUEST));
        if (emailVerification.getExpiredDate().isAfter(LocalDateTime.now()) &&
                emailVerification.getCode().equals(certificationDto.getCode())) {
            return;
        }
        throw new BaseException(BaseResponseStatus.EXPIRED_CODE);
    }

    public TokenDto login(LoginDto loginDto) throws BaseException {
        Member member = memberRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USERS_ID));
        if (member.getStatus().equals(BaseEntity.Status.EXPIRED)) {
            throw new BaseException(BaseResponseStatus.EXPIRED_USERS);
        }
        checkPassword(loginDto.getPassword(), member.getPassword());
        String accessToken = jwtTokenUtil.generateAccessToken(member.getEmail());
        RefreshToken refreshToken = saveRefreshToken(member.getEmail());
        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken.getRefreshToken())
                .grantType(JwtHeaderUtil.GRANT_TYPE.getValue()).build();
    }

    public void checkPassword(String rawPassword, String findMemberPassword) throws BaseException {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new BaseException(BaseResponseStatus.INVALID_PASSWORD);
        }
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
                jwtTokenUtil.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
    }
}
