package v1.amachon.project.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import v1.amachon.common.IntegrationTest;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.fixtures.ProjectFixtures;
import v1.amachon.fixtures.TagFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.entity.Project;
import v1.amachon.project.entity.RecruitManagement;
import v1.amachon.project.entity.TeamMember;
import v1.amachon.project.repository.MemberRecommendRepo;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.repository.RecruitManagementRepository;
import v1.amachon.project.repository.TeamMemberRepository;
import v1.amachon.project.service.exception.FailureProjectModifyException;
import v1.amachon.project.service.exception.ProjectApplyDeniedException;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;

import java.util.List;

import static v1.amachon.fixtures.MemberFixtures.정우_이메일;

public class RecruitProjectServiceTest extends IntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecruitManagementRepository recruitManagementRepository;

    @Autowired
    private MemberRecommendRepo memberRecommendRepo;

    @Autowired
    private RecruitProjectService recruitProjectService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private RegionTagRepository regionTagRepository;

    @BeforeEach
    void setUp() {
        TechTag backend = techTagRepository.save(TagFixtures.backend());
        TechTag spring = techTagRepository.save(TagFixtures.spring(backend));

        TechTag frontend = techTagRepository.save(TagFixtures.frontend());
        TechTag android = techTagRepository.save(TagFixtures.android(frontend));

        RegionTag gyeonggi = regionTagRepository.save(TagFixtures.gyeonggi());
        RegionTag hwaseong = regionTagRepository.save(TagFixtures.hwaseong(gyeonggi));

        Member 정우 = memberRepository.save(MemberFixtures.정우());
        Member 종범 = memberRepository.save(MemberFixtures.종범());

        Project project = ProjectFixtures.정우_프로젝트(정우);
        project.updateTechTags(List.of(new ProjectTechTag(project, spring), new ProjectTechTag(project, android)));
        project.updateRegionTag(hwaseong);

        projectRepository.save(project);
    }

    @DisplayName("프로젝트 참여 지원에 성공한다")
    @WithMockUser(value = MemberFixtures.종범_이메일)
    @Test
    void applySuccess() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();

        // when
        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);
        RecruitManagement actual = recruitManagementRepository.findByProjectId(projectId).get(0);

        // then
        Assertions.assertThat(actual.getMember().getEmail()).isEqualTo(MemberFixtures.종범_이메일);
    }

    @DisplayName("이미 프로젝트 지원한 사용자는 지원에 실패한다")
    @WithMockUser(value = MemberFixtures.종범_이메일)
    @Test
    void applyFailByDuplicateApplicant() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();
        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);

        // when, then
        Assertions.assertThatThrownBy(() -> recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일))
                .isInstanceOf(ProjectApplyDeniedException.class)
                .hasMessage("이미 팀원이거나 지원 요청이 존재합니다.");
    }

    @DisplayName("이미 프로젝트의 팀원인 사용자는 지원에 실패한다")
    @WithMockUser(value = MemberFixtures.종범_이메일)
    @Test
    void applyFailByAlreadyTeamMember() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();
        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);
        RecruitManagement recruitManagement = recruitManagementRepository.findByProjectId(projectId).get(0);
        recruitProjectService.recruitAccept(recruitManagement.getId());

        // when, then
        Assertions.assertThatThrownBy(() -> recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일))
                .isInstanceOf(ProjectApplyDeniedException.class)
                .hasMessage("이미 팀원이거나 지원 요청이 존재합니다.");
    }

    @DisplayName("프로젝트 리더는 자신의 프로젝트에 지원할 수 없다")
    @WithMockUser(value = MemberFixtures.정우_이메일)
    @Test
    void applyFailByProjectLeader() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();

        // when, then
        Assertions.assertThatThrownBy(() -> recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("프로젝트 리더는 지원 신청을 수락할 수 있다")
    @WithMockUser(value = MemberFixtures.정우_이메일)
    @Test
    void successRecruitAccept() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();
        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);
        RecruitManagement recruitManagement = recruitManagementRepository.findByProjectId(projectId).get(0);

        // when
        recruitProjectService.recruitAccept(recruitManagement.getId());
        TeamMember teamMember = teamMemberRepository.findByProjectId(projectId).get(0);

        // then
        Assertions.assertThat(teamMember.getMember().getEmail()).isEqualTo(MemberFixtures.종범_이메일);
    }

    @DisplayName("프로젝트 리더가 아닌 사람은 지원 신청을 수락할 수 없다")
    @WithMockUser(value = MemberFixtures.종범_이메일)
    @Test
    void failRecruitAccept() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();
        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);
        RecruitManagement recruitManagement = recruitManagementRepository.findByProjectId(projectId).get(0);

        // when, then
        Assertions.assertThatThrownBy(() -> recruitProjectService.recruitAccept(recruitManagement.getId()))
                .isInstanceOf(FailureProjectModifyException.class)
                .hasMessage("프로젝트를 수정하거나 삭제할 권한이 없습니다.");
    }

    @DisplayName("프로젝트 리더는 지원 신청을 거절할 수 있다")
    @WithMockUser(value = MemberFixtures.정우_이메일)
    @Test
    void successRecruitReject() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();
        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);
        RecruitManagement recruitManagement = recruitManagementRepository.findByProjectId(projectId).get(0);

        // when
        recruitProjectService.recruitReject(recruitManagement.getId());

        // then
        Assertions.assertThat(recruitManagement.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED);
    }

    @DisplayName("프로젝트 리더가 아닌 사람은 지원 신청을 거절할 수 없다")
    @WithMockUser(value = MemberFixtures.종범_이메일)
    @Test
    void failRecruitReject() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();
        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);
        RecruitManagement recruitManagement = recruitManagementRepository.findByProjectId(projectId).get(0);

        // when, then
        Assertions.assertThatThrownBy(() -> recruitProjectService.recruitReject(recruitManagement.getId()))
                .isInstanceOf(FailureProjectModifyException.class)
                .hasMessage("프로젝트를 수정하거나 삭제할 권한이 없습니다.");
    }

    @DisplayName("프로젝트 리더는 팀원을 추방할 수 있다")
    @WithMockUser(value = MemberFixtures.정우_이메일)
    @Test
    void successKickTeamMember() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();

        recruitProjectService.applyForProject(projectId, MemberFixtures.종범_이메일);
        RecruitManagement recruitManagement = recruitManagementRepository.findByProjectId(projectId).get(0);

        recruitProjectService.recruitAccept(recruitManagement.getId());
        TeamMember teamMember = teamMemberRepository.findByProjectId(projectId).get(0);

        // when
        recruitProjectService.kickTeamMember(teamMember.getId());

        // then
        Assertions.assertThat(teamMember.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED);
    }

}
