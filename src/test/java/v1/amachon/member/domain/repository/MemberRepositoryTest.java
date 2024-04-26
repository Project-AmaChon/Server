package v1.amachon.member.domain.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import v1.amachon.common.RepositoryTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.entity.vo.Profile;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RepositoryTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(MemberFixtures.정우());
        memberRepository.save(MemberFixtures.종범());
        memberRepository.save(MemberFixtures.규범());
        memberRepository.save(MemberFixtures.승현());
        memberRepository.save(MemberFixtures.재욱());
    }

    @Test
    @DisplayName("이메일을 통해 사용자를 조회할 수 있다.")
    public void findMember() {
        // when
        Member member = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();

        // then
        assertAll(
                () -> assertThat(member.getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(MemberFixtures.정우_이메일),
                () -> assertThat(member.getNickname()).isEqualTo(MemberFixtures.정우_닉네임),
                () -> assertThat(member.getPassword()).isEqualTo(MemberFixtures.정우_비밀번호)
        );
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 조회한다.")
    public void findNotExistMember() {
        // when
        Optional<Member> member = memberRepository.findByEmail("notExist@naver.com");

        // then
        Assertions.assertThat(member).isEmpty();
    }

    @Test
    @DisplayName("사용자의 프로필을 수정할 수 있다.")
    public void updateMemberProfile() {
        // given
        Member member = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        Profile profile = member.getProfile();
        UpdateProfileRequestDto updateProfileDto = new UpdateProfileRequestDto("안녕하세요. 이정우입니다.",
                "저는 서버 개발을 하고 있어요!", "https://github.com/dlwjddn123", "acg6138@tistory.com");

        // when
        member.changeProfile(updateProfileDto);
        memberRepository.save(member);

        // then
        assertAll(
                () -> assertThat(member).isNotNull(),
                () -> assertThat(profile.getIntroduction()).isEqualTo("안녕하세요. 이정우입니다."),
                () -> assertThat(profile.getDescription()).isEqualTo("저는 서버 개발을 하고 있어요!"),
                () -> assertThat(profile.getBlogUrl()).isEqualTo("acg6138@tistory.com"),
                () -> assertThat(profile.getGithubUrl()).isEqualTo("https://github.com/dlwjddn123")
        );
    }

    @Test
    @DisplayName("사용자의 프로필 이미지를 수정할 수 있다.")
    public void updateMemberProfileImageUrl() {
        // given
        Member member = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        Profile profile = member.getProfile();
        String updateProfileImageUrl = "updateImageUrl";

        // when
        member.changeProfileImage(updateProfileImageUrl);
        memberRepository.save(member);

        // then
        Assertions.assertThat(profile.getProfileImageUrl()).isEqualTo(updateProfileImageUrl);
    }
}
