package v1.amachon.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.member.entity.vo.Profile;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberTest {

    @Test
    @DisplayName("사용자의 프로필을 수정할 수 있다.")
    public void updateMemberProfile() {
        // given
        Member member = MemberFixtures.정우();
        Profile profile = member.getProfile();
        UpdateProfileRequestDto updateProfileDto = new UpdateProfileRequestDto("안녕하세요. 이정우입니다.",
                "저는 서버 개발을 하고 있어요!", "https://github.com/dlwjddn123", "acg6138@tistory.com");

        // when
        member.changeProfile(updateProfileDto);

        // then
        assertAll(
                () -> assertThat(member).isNotNull(),
                () -> assertThat(profile.getIntroduction()).isEqualTo("안녕하세요. 이정우입니다."),
                () -> assertThat(profile.getDescription()).isEqualTo("저는 서버 개발을 하고 있어요!"),
                () -> assertThat(profile.getBlogUrl()).isEqualTo("acg6138@tistory.com"),
                () -> assertThat(profile.getGithubUrl()).isEqualTo("https://github.com/dlwjddn123")
        );
    }
}
