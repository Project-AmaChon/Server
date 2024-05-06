package v1.amachon.project.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import v1.amachon.common.IntegrationTest;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.fixtures.ProjectFixtures;
import v1.amachon.fixtures.TagFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.entity.Project;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.service.exception.FailureProjectModifyException;
import v1.amachon.project.service.request.CreateProjectRequest;
import v1.amachon.project.service.request.UpdateProjectRequest;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.ProjectTechTagRepository;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static v1.amachon.common.entity.BaseEntity.Status.EXPIRED;

public class ProjectServiceTest extends IntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private RegionTagRepository regionTagRepository;

    @Autowired
    private ProjectTechTagRepository projectTechTagRepository;

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

    @DisplayName("로그인 되어있는 유저는 프로젝트 게시글을 생성할 수 있다")
    @WithMockUser(value = MemberFixtures.정우_이메일)
    @Test
    void createProject() {
        // given
        CreateProjectRequest request = CreateProjectRequest.builder().title(ProjectFixtures.정우_프로젝트_제목)
                .description(ProjectFixtures.정우_프로젝트_설명)
                .recruitDeadline(ProjectFixtures.정우_프로젝트_마감_기한)
                .developPeriod(ProjectFixtures.정우_프로젝트_진행_기간)
                .recruitNumber(ProjectFixtures.정우_프로젝트_모집_인원)
                .regionTagName(TagFixtures.hwaseong)
                .techTagNames(List.of(TagFixtures.spring))
                .build();

        // when
        Long projectId = projectService.createProject(request);
        Optional<Project> project = projectRepository.findById(projectId);

        // then
        Assertions.assertThat(project).isNotEmpty();
    }

    @DisplayName("게시글 작성자는 프로젝트 게시글을 수정할 수 있다")
    @WithMockUser(value = MemberFixtures.정우_이메일)
    @Test
    void updateProject() {
        // given
        Member leader = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        Project project = projectRepository.findByLeaderId(leader.getId()).get(0);
        UpdateProjectRequest request = UpdateProjectRequest.builder().title(ProjectFixtures.정우_프로젝트_제목)
                .description(ProjectFixtures.정우_프로젝트_설명)
                .recruitDeadline(ProjectFixtures.정우_프로젝트_마감_기한)
                .developPeriod(ProjectFixtures.정우_프로젝트_진행_기간)
                .recruitNumber(ProjectFixtures.정우_프로젝트_모집_인원)
                .regionTagName(TagFixtures.hwaseong)
                .techTagNames(List.of(TagFixtures.spring))
                .build();

        // when
        projectService.updateProject(project.getId(), request);

        // then
        assertAll(
                () -> assertThat(project.getTitle()).isEqualTo(request.getTitle()),
                () -> assertThat(project.getDescription()).isEqualTo(request.getDescription()),
                () -> assertThat(project.getRecruitDeadline()).isEqualTo(request.getRecruitDeadline()),
                () -> assertThat(project.getDevelopPeriod()).isEqualTo(request.getDevelopPeriod()),
                () -> assertThat(project.getRecruitNumber()).isEqualTo(request.getRecruitNumber())
        );
    }

    @DisplayName("게시글 작성자가 아닌 유저가 프로젝트 게시글을 수정하는 경우 401 에러가 발생한다")
    @WithMockUser(value = MemberFixtures.종범_이메일)
    @Test
    void failUpdateProjectByOtherUsers() {
        // given
        Member leader = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        Project project = projectRepository.findByLeaderId(leader.getId()).get(0);
        UpdateProjectRequest request = UpdateProjectRequest.builder().title(ProjectFixtures.정우_프로젝트_제목)
                .description(ProjectFixtures.정우_프로젝트_설명)
                .recruitDeadline(ProjectFixtures.정우_프로젝트_마감_기한)
                .developPeriod(ProjectFixtures.정우_프로젝트_진행_기간)
                .recruitNumber(ProjectFixtures.정우_프로젝트_모집_인원)
                .regionTagName(TagFixtures.hwaseong)
                .techTagNames(List.of(TagFixtures.spring))
                .build();

        // when, then
        Assertions.assertThatThrownBy(() -> projectService.updateProject(project.getId(), request))
                .isInstanceOf(FailureProjectModifyException.class)
                .hasMessage("게시글을 수정하거나 삭제할 권한이 없습니다.");
    }

    @DisplayName("게시글 작성자는 프로젝트 게시글을 삭제할 수 있다")
    @WithMockUser(value = MemberFixtures.정우_이메일)
    @Test
    void deleteProject() {
        // given
        Member leader = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        Project project = projectRepository.findByLeaderId(leader.getId()).get(0);

        // when
        projectService.deleteProject(project.getId());

        // then
        Assertions.assertThat(project.getStatus()).isEqualTo(EXPIRED);
    }

    @DisplayName("게시글 작성자가 아닌 유저가 프로젝트 게시글을 삭제하는 경우 401 에러가 발생한다")
    @WithMockUser(value = MemberFixtures.종범_이메일)
    @Test
    void deleteProjectByOtherUsers() {
        // given
        Member leader = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();
        Project project = projectRepository.findByLeaderId(leader.getId()).get(0);

        // when, then
        Assertions.assertThatThrownBy(() -> projectService.deleteProject(project.getId()))
                .isInstanceOf(FailureProjectModifyException.class)
                .hasMessage("게시글을 수정하거나 삭제할 권한이 없습니다.");
    }

}
