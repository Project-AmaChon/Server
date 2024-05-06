package v1.amachon.project.domain.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import v1.amachon.common.RepositoryTest;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.entity.Project;
import v1.amachon.project.entity.RecruitManagement;
import v1.amachon.project.entity.TeamMember;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.repository.ProjectSearchRepository;
import v1.amachon.project.repository.RecruitManagementRepository;
import v1.amachon.project.repository.TeamMemberRepository;
import v1.amachon.project.service.request.ProjectSearchCond;
import v1.amachon.project.service.response.ProjectResponse;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;

@RepositoryTest
public class ProjectSearchRepositoryTest {

    @Autowired
    private ProjectSearchRepository projectSearchRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private RecruitManagementRepository recruitManagementRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private RegionTagRepository regionTagRepository;

    @BeforeEach
    void setUp() {
        Member leader = memberRepository.save(MemberFixtures.정우());
        Member teamMember = memberRepository.save(MemberFixtures.종범());
        Member participant = memberRepository.save(MemberFixtures.재욱());

        TechTag spring = techTagRepository.save(new TechTag("spring", 0, null));
        RegionTag regionTag = regionTagRepository.save(new RegionTag("화성시", 0, null));

        Project project = createProject(leader, regionTag);
        project.addTeamMember(new TeamMember(project, teamMember));
        project.updateTechTags(List.of(new ProjectTechTag(project, spring)));
        project.apply(participant);

        projectRepository.save(project);
    }

    @DisplayName("프로젝트를 삭제하면 관련된 모든 엔티티의 Status 는 EXPIRED 로 변경된다")
    @Test
    void deleteProject() {
        // given
        Project project = projectRepository.findAll().get(0);
        RecruitManagement recruitManagement = project.getRecruitManagements().get(0);
        ProjectTechTag projectTechTag = project.getTechTags().get(0);
        TeamMember teamMember = project.getTeamMembers().get(0);

        // when
        project.delete();

        assertAll(
                () -> Assertions.assertThat(project.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED),
                () -> Assertions.assertThat(recruitManagement.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED),
                () -> Assertions.assertThat(projectTechTag.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED),
                () -> Assertions.assertThat(teamMember.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED)
        );
    }

    @DisplayName("존재하지 않는 프로젝트를 조회한다")
    @Test
    void notExistProject() {
        // when
        Optional<Project> project = projectRepository.findById(-1L);

        // then
        Assertions.assertThat(project).isEmpty();
    }

    @DisplayName("프로젝트 제목으로 프로젝트를 조회할 수 있다")
    @Test
    void findByTitle() {
        // given
        String title = "스프링 프로젝트";
        ProjectSearchCond cond = ProjectSearchCond.builder().keyword(title).build();

        // when
        ProjectResponse projectResponse = projectSearchRepository.searchProjectByAllCond(cond).get(0);

        // then
        Assertions.assertThat(projectResponse.getTitle()).isEqualTo(title);
    }

    @DisplayName("프로젝트의 기술 태그 이름으로 프로젝트를 조회할 수 있다")
    @Test
    void findByTechTagName() {
        // given
        String techTagName = "spring";
        ProjectSearchCond cond = ProjectSearchCond.builder().techTagNames(List.of(techTagName)).build();

        // when
        ProjectResponse projectResponse = projectSearchRepository.searchProjectByAllCond(cond).get(0);

        // then
        Assertions.assertThat(projectResponse.getTagNames().get(0)).isEqualTo(techTagName);
    }

    @DisplayName("프로젝트의 지역 태그 이름으로 프로젝트를 조회할 수 있다")
    @Test
    void findByRegionTagName() {
        // given
        String regionTagName = "화성시";
        ProjectSearchCond cond = ProjectSearchCond.builder().regionTagNames(List.of(regionTagName)).build();

        // when
        ProjectResponse projectResponse = projectSearchRepository.searchProjectByAllCond(cond).get(0);

        // then
        Assertions.assertThat(projectResponse.getRegionTagName()).isEqualTo(regionTagName);
    }

    @DisplayName("프로젝트의 제목, 기술 태그, 지역 태그 이름를 이용하여 동적으로 프로젝트를 조회할 수 있다")
    @ParameterizedTest
    @MethodSource("provideProjectSearchCond")
    void findByAllCond(String title, List<String> regionTagNames, List<String> techTagNames) {
        // given
        Project expected = projectRepository.findAll().get(0);

        ProjectSearchCond cond = ProjectSearchCond.builder()
                .keyword(title)
                .regionTagNames(regionTagNames)
                .techTagNames(techTagNames).build();

        // when
        ProjectResponse projectResponse = projectSearchRepository.searchProjectByAllCond(cond).get(0);
        Project actual = projectRepository.findById(projectResponse.getProjectId()).get();

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }


    private Project createProject(Member leader, RegionTag regionTag) {
        return Project.builder()
                .leader(leader)
                .title("스프링 프로젝트")
                .description("프로젝트 매칭 서비스")
                .developPeriod(LocalDate.now())
                .recruitDeadline(LocalDate.now().plusMonths(1l))
                .recruitNumber(5)
                .regionTag(regionTag)
                .build();
    }

    private static Stream<Arguments> provideProjectSearchCond() {
        return Stream.of(
                Arguments.of("스프링 프로젝트", null, null),
                Arguments.of("스프링 프로젝트", List.of("화성시"), null),
                Arguments.of("스프링 프로젝트", null, List.of("spring")),
                Arguments.of("스프링 프로젝트", List.of("화성시"), List.of("spring")),
                Arguments.of(null, List.of("화성시"), null),
                Arguments.of(null, List.of("화성시"), List.of("spring")),
                Arguments.of(null, null, List.of("spring"))
        );
    }
}
