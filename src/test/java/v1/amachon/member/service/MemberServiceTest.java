package v1.amachon.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.common.IntegrationTest;
import v1.amachon.common.config.s3.S3UploadUtil;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.fixtures.TagFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.member.service.dto.ProfileResponseDto;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;
import v1.amachon.member.service.dto.join.JoinRequest;
import v1.amachon.member.service.exception.DuplicatedEmailException;
import v1.amachon.member.service.exception.DuplicatedNicknameException;
import v1.amachon.member.service.exception.NotFoundMemberException;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.MemberTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.MemberTechTagRepository;
import v1.amachon.tags.repository.ProjectTechTagRepository;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class MemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RegionTagRepository regionTagRepository;

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private MemberTechTagRepository memberTechTagRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectTechTagRepository projectTechTagRepository;

    @MockBean
    public S3UploadUtil s3UploadUtil;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, passwordEncoder, s3UploadUtil);
        Member member = memberRepository.save(MemberFixtures.정우());
        setupMemberTags(member);
    }

    @DisplayName("회원 가입 정보가 올바르면 회원 가입에 성공한다")
    @Test
    void joinSuccess() {
        // given
        JoinRequest joinRequest = JoinRequest.builder().email(MemberFixtures.종범_이메일)
                .password(MemberFixtures.종범_비밀번호)
                .nickname(MemberFixtures.종범_닉네임)
                .build();

        // when
        memberService.join(joinRequest);

        // then
        Assertions.assertThat(memberRepository.findByEmail(MemberFixtures.종범_이메일).get()).isNotNull();
    }

    @DisplayName("가입하려는 이메일이 이미 존재한다면 회원 가입 시 예외가 발생한다")
    @Test
    void joinFailByDuplicateEmail() {
        // given
        JoinRequest joinRequest = JoinRequest.builder().email(MemberFixtures.정우_이메일)
                .password(MemberFixtures.정우_비밀번호)
                .nickname(MemberFixtures.종범_닉네임)
                .build();

        // when, then
        Assertions.assertThatThrownBy(() -> memberService.join(joinRequest))
                .isInstanceOf(DuplicatedEmailException.class);
    }

    @DisplayName("가입하려는 닉네임이 이미 존재한다면 회원 가입 시 예외가 발생한다")
    @Test
    void joinFailByDuplicateNickname() {
        // given
        JoinRequest joinRequest = JoinRequest.builder().email(MemberFixtures.종범_이메일)
                .password(MemberFixtures.종범_비밀번호)
                .nickname(MemberFixtures.정우_닉네임)
                .build();

        // when, then
        Assertions.assertThatThrownBy(() -> memberService.join(joinRequest))
                .isInstanceOf(DuplicatedNicknameException.class);
    }

    @DisplayName("로그인 되어있는 회원은 마이프로필을 조회할 수 있다")
    @WithMockUser(username = MemberFixtures.정우_이메일)
    @Test
    void successGetMyPage() {
        // given
        Member currentMember = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        List<String> techTags = currentMember.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList());
        ProfileResponseDto expected = new ProfileResponseDto(currentMember.getProfile(), currentMember.getNickname(), techTags, currentMember.getRegionTag().getName());

        // when
        ProfileResponseDto actual = memberService.getMyProfile();

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("로그인 되어있지 않은 회원은 마이프로필을 조회 시 에외가 발생한다")
    @WithAnonymousUser
    @Test
    void failGetMyPageWithAnonymousUser() {
        // when, then
        Assertions.assertThatThrownBy(() -> memberService.getMyProfile())
                .isInstanceOf(UnauthorizedException.class);
    }

    @DisplayName("프로필을 조회할 수 있다")
    @Test
    void successGetProfile() {
        // given
        Member member = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        List<String> techTags = member.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList());
        ProfileResponseDto expected = new ProfileResponseDto(member.getProfile(), member.getNickname(), techTags, member.getRegionTag().getName());

        // when
        ProfileResponseDto actual = memberService.getProfile(member.getId());

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 회원의 프로필을 조회 시 예외가 발생한다")
    @Test
    void failGetProfile() {
        // when, then
        Assertions.assertThatThrownBy(() -> memberService.getProfile(9999L))
                .isInstanceOf(NotFoundMemberException.class);
    }

    @DisplayName("프로필 이미지를 변경할 수 있다")
    @WithMockUser(username = MemberFixtures.정우_이메일)
    @Test
    void changeProfileImage() {
        // given
        String imageUrl = "mockImageUrl";
        MultipartFile imageFile = mock(MultipartFile.class);
        given(s3UploadUtil.upload(any(MultipartFile.class))).willReturn(imageUrl);

        // when
        memberService.changeProfileImage(imageFile);
        Member currentMember = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();

        // then
        Assertions.assertThat(currentMember.getProfile().getProfileImageUrl()).isEqualTo(imageUrl);
    }

    @DisplayName("프로필을 변경할 수 있다")
    @WithMockUser(username = MemberFixtures.정우_이메일)
    @Test
    void changeProfile() {
        // given
        UpdateProfileRequestDto request = UpdateProfileRequestDto.builder()
                .blogUrl("myBlog.com")
                .description("백엔드 개발중입니다~")
                .githubUrl("myGithub.com")
                .introduction("안녕하세요.")
                .build();

        // when
        memberService.changeProfile(request);
        Member currentMember = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();

        // then
        Assertions.assertThat(currentMember.getProfile())
                .extracting("introduction", "description", "githubUrl", "blogUrl")
                .containsExactly(request.getIntroduction(), request.getDescription(), request.getGithubUrl(), request.getBlogUrl());
    }

    void setupMemberTags(Member member) {
        TechTag backend = techTagRepository.save(TagFixtures.backend());
        TechTag spring = techTagRepository.save(TechTag.builder().depth(1).name(TagFixtures.spring).parent(backend).build());
        TechTag nodeJS = techTagRepository.save(TechTag.builder().depth(1).name(TagFixtures.nodejs).parent(backend).build());
        TechTag django = techTagRepository.save(TechTag.builder().depth(1).name(TagFixtures.django).parent(backend).build());

        List<MemberTechTag> techTags = List.of(spring, nodeJS, django).stream()
                .map(t -> memberTechTagRepository.save(new MemberTechTag(member, t)))
                .collect(Collectors.toList());

        member.changeTechTag(techTags);

        RegionTag seoul = regionTagRepository.save(RegionTag.builder().depth(0).name("서울").build());
        RegionTag gangnam_gu = regionTagRepository.save(RegionTag.builder().parent(seoul).depth(1).name("강남구").build());

        member.changeRegionTag(gangnam_gu);
        memberRepository.save(member);
    }

}
