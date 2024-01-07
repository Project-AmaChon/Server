package v1.amachon.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.common.config.s3.S3UploadUtil;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.member.service.dto.ProfileResponseDto;
import v1.amachon.member.service.dto.join.JoinDto;
import v1.amachon.member.service.dto.join.NicknameDto;
import v1.amachon.member.service.dto.login.LoginDto;
import v1.amachon.member.service.dto.login.TokenDto;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.member.service.exception.DuplicatedEmailException;
import v1.amachon.member.service.exception.DuplicatedNicknameException;
import v1.amachon.member.service.exception.NotFoundMemberException;
import v1.amachon.common.config.security.util.SecurityUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadUtil s3UploadUtil;

    public TokenDto join(JoinDto joinDto) {
        isDuplicateEmail(joinDto.getEmail());
        String rawPassword = joinDto.getPassword();
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        memberRepository.save(Member.ofMember(joinDto));
        return authService.login(LoginDto.builder().email(joinDto.getEmail())
                .password(rawPassword).build());
    }

    public void isDuplicateEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (!member.isEmpty()) {
            throw new DuplicatedEmailException();
        }
    }

    public void isDuplicateNickname(NicknameDto nicknameDto) {
        Optional<Member> member = memberRepository.findByNickname(nicknameDto.getNickname());
        if (!member.isEmpty()) {
            throw new DuplicatedNicknameException();
        }
    }

    public ProfileResponseDto getMyProfile() {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        List<String> techTags = member.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList());
        return new ProfileResponseDto(member.getProfile(), member.getNickname(), techTags, member.getRegionTag().getName());
    }

    public ProfileResponseDto getProfile(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);
        List<String> techTags = member.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList());
        return new ProfileResponseDto(member.getProfile(), member.getNickname(), techTags, member.getRegionTag().getName());
    }

    public void changeProfileImage(MultipartFile profileImage) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        String profileImageUrl = s3UploadUtil.upload(profileImage);

        if (!member.getProfile().getProfileImageUrl().isEmpty()) {
            s3UploadUtil.fileDelete(member.getProfile().getProfileImageUrl());
        }
        member.getProfile().changeProfileImage(profileImageUrl);
        memberRepository.save(member);
    }

    public void changeProfile(ProfileResponseDto profileResponseDto) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        member.changeProfile(profileResponseDto);
        memberRepository.save(member);
    }
}
