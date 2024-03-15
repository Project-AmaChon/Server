package v1.amachon.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import v1.amachon.common.RepositoryTest;
import v1.amachon.member.service.dto.ProfileResponseDto;
import v1.amachon.member.entity.Member;
import v1.amachon.member.entity.Profile;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.fixtures.MemberFixtures;

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
    @DisplayName("이메일을 통해 사용자를 조회한다.")
    public void findMember() {
        Member member = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();

        assertAll(
                () -> assertThat(member.getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(MemberFixtures.정우_이메일),
                () -> assertThat(member.getNickname()).isEqualTo(MemberFixtures.정우_닉네임),
                () -> assertThat(member.getPassword()).isEqualTo(MemberFixtures.정우_비밀번호)
        );
    }

    @Test
    @DisplayName("사용자의 프로필을 수정한다.")
    public void updateMemberProfile() {
        ProfileResponseDto updateProfileDto = new ProfileResponseDto("안녕하세요. 이정우입니다.", "이정우.png", "저는 서버 개발을 하고 있어요!", "dlwjddn123", "acg6138@tistory.com", "이정우유");
        Member member = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        Profile profile = member.getProfile();
        member.changeProfile(updateProfileDto);

        assertAll(
                () -> assertThat(member.getId()).isNotNull(),
                () -> assertThat(member.getNickname()).isEqualTo("이정우유"),
                () -> assertThat(profile.getIntroduction()).isEqualTo("안녕하세요. 이정우입니다."),
                () -> assertThat(profile.getProfileImageUrl()).isEqualTo("이정우.png"),
                () -> assertThat(profile.getDescription()).isEqualTo("저는 서버 개발을 하고 있어요!"),
                () -> assertThat(profile.getBlogUrl()).isEqualTo("acg6138@tistory.com"),
                () -> assertThat(profile.getGithubUrl()).isEqualTo("dlwjddn123")
        );
    }
}
