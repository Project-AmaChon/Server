package v1.amachon.project.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.project.entity.Project;
import v1.amachon.project.entity.RecruitManagement;
import v1.amachon.project.entity.TeamMember;
import v1.amachon.project.service.exception.ProjectApplyDeniedException;
import v1.amachon.project.service.request.UpdateProjectRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProjectTest {

    @DisplayName("프로젝트 게시글 생성자(리더)가 아니고, 해당 프로젝트의 팀원이 아니라면 지원할 수 있다")
    @Test
    void applySuccess() {
        // given
        Member leader = MemberFixtures.정우();
        Member participant = MemberFixtures.종범();
        Project project = createProject(leader);

        // when
        project.apply(participant);
        RecruitManagement recruitManagement = project.getRecruitManagements().get(0);

        // then
        Assertions.assertThat(recruitManagement.getMember()).isEqualTo(participant);
    }

    @DisplayName("프로젝트 게시글 생성자(리더)는 해당 프로젝트에 지원할 수 없다")
    @Test
    void applyFailByParticipantEqualsLeader() {
        // given
        Member leader = MemberFixtures.정우();
        Member participant = MemberFixtures.정우();
        Project project = createProject(leader);

        // when, then
        Assertions.assertThatThrownBy(() -> project.apply(participant))
                        .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("프로젝트의 팀원은 해당 프로젝트에 중복으로 지원할 수 없다")
    @Test
    void applyFailByParticipantEqualsTeamMember() {
        // given
        Member leader = MemberFixtures.정우();
        Member teamMember = MemberFixtures.종범();
        Project project = createProject(leader);
        project.addTeamMember(new TeamMember(project, teamMember));

        // when, then
        Assertions.assertThatThrownBy(() -> project.apply(teamMember))
                .isInstanceOf(ProjectApplyDeniedException.class)
                .hasMessage("이미 팀원이거나 지원 요청이 존재합니다.");
    }

    @DisplayName("프로젝트를 수정할 수 있다")
    @Test
    void modifyProject() {
        // given
        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .title("수정된 정우 프로젝트")
                .description("업그레이드 된 프로젝트 매칭 서비스")
                .recruitDeadline(LocalDate.now())
                .developPeriod(LocalDate.now().plusMonths(1l))
                .recruitNumber(4)
                .regionTagName("강남구").build();

        Project project = createProject(MemberFixtures.정우());

        // when
        project.updateProject(request);
        // then
        assertAll(
                () -> assertThat(project.getTitle()).isEqualTo(request.getTitle()),
                () -> assertThat(project.getDescription()).isEqualTo(request.getDescription()),
                () -> assertThat(project.getRecruitDeadline()).isEqualTo(request.getRecruitDeadline()),
                () -> assertThat(project.getDevelopPeriod()).isEqualTo(request.getDevelopPeriod()),
                () -> assertThat(project.getRecruitNumber()).isEqualTo(request.getRecruitNumber())
        );
    }

    private Project createProject(Member leader) {
        return Project.builder()
                .leader(leader)
                .title("정우 프로젝트")
                .description("프로젝트 매칭 서비스")
                .developPeriod(LocalDate.now())
                .recruitDeadline(LocalDate.now().plusMonths(1l))
                .recruitNumber(5)
                .build();
    }
}
