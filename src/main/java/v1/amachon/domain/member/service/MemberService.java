package v1.amachon.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.member.dto.ProfileDto;
import v1.amachon.domain.member.dto.join.JoinDto;
import v1.amachon.domain.member.dto.join.NicknameDto;
import v1.amachon.domain.member.dto.login.LoginDto;
import v1.amachon.domain.member.dto.login.TokenDto;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.global.config.s3.S3UploadUtil;
import v1.amachon.global.config.security.util.SecurityUtils;

import java.io.IOException;
import java.util.Optional;

import static v1.amachon.domain.base.BaseResponseStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadUtil s3UploadUtil;

    public TokenDto join(JoinDto joinDto) throws BaseException {
        String rawPassword = joinDto.getPassword();
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        memberRepository.save(Member.ofMember(joinDto));
        return authService.login(LoginDto.builder().email(joinDto.getEmail())
                .password(rawPassword).build());
    }

    public void isDuplicateEmail(String email) throws BaseException {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (!member.isEmpty()) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_EMAIL);
        }
    }

    public void isDuplicateNickname(NicknameDto nicknameDto) throws BaseException {
        Optional<Member> member = memberRepository.findByNickname(nicknameDto.getNickname());
        if (!member.isEmpty()) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_NICKNAME);
        }
    }

    public ProfileDto getProfile() throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        return new ProfileDto(member.getProfile());
    }

    public void changeProfileImage(MultipartFile profileImage) throws BaseException, IOException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        String profileImageUrl = s3UploadUtil.upload(profileImage);

        if (!member.getProfile().getProfileImageUrl().isEmpty()) {
            s3UploadUtil.fileDelete(member.getProfile().getProfileImageUrl());
        }
        member.getProfile().changeProfileImage(profileImageUrl);
        memberRepository.save(member);
    }

    public void changeProfile(ProfileDto profileDto) throws BaseException, IOException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        member.changeProfile(profileDto);
        memberRepository.save(member);
    }
}
