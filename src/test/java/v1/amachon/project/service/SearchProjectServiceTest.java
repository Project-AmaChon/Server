package v1.amachon.project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import v1.amachon.common.IntegrationTest;
import v1.amachon.fixtures.MemberFixtures;
import v1.amachon.fixtures.ProjectFixtures;
import v1.amachon.fixtures.TagFixtures;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.entity.Project;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.service.response.ProjectDetailResponse;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.ProjectTechTagRepository;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;
import v1.amachon.tags.service.RegionTagService;
import v1.amachon.tags.service.TechTagService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static v1.amachon.fixtures.MemberFixtures.정우_이메일;

public class SearchProjectServiceTest extends IntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SearchProjectService searchProjectService;

    @Autowired
    private RegionTagService regionTagService;

    @Autowired
    private TechTagService techTagService;

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

        Project project = ProjectFixtures.정우_프로젝트(정우);
        project.updateTechTags(List.of(new ProjectTechTag(project, spring), new ProjectTechTag(project, android)));
        project.updateRegionTag(hwaseong);

        projectRepository.save(project);
    }

    @DisplayName("프로젝트의 상세 정보를 가져올 수 있다")
    @Test
    void getProjectDetail() {
        // given
        Member leader = memberRepository.findByEmail(정우_이메일).get();
        Long projectId = projectRepository.findByLeaderId(leader.getId()).get(0).getId();

        // when
        ProjectDetailResponse actual = searchProjectService.getProjectDetail(projectId);

        // then
        assertAll(
                () -> assertThat(ProjectFixtures.정우_프로젝트_제목).isEqualTo(actual.getTitle()),
                () -> assertThat(ProjectFixtures.정우_프로젝트_설명).isEqualTo(actual.getDescription()),
                () -> assertThat(ProjectFixtures.정우_프로젝트_마감_기한).isEqualTo(actual.getRecruitDeadline()),
                () -> assertThat(ProjectFixtures.정우_프로젝트_진행_기간).isEqualTo(actual.getDevelopPeriod()),
                () -> assertThat(ProjectFixtures.정우_프로젝트_모집_인원).isEqualTo(actual.getRecruitNumber()),
                () -> assertThat(leader.getId()).isEqualTo(actual.getLeaderId()),
                () -> assertThat(TagFixtures.hwaseong).isEqualTo(actual.getRegionTagName()),
                () -> assertThat(List.of(TagFixtures.spring, TagFixtures.android)).isEqualTo(actual.getTechTagNames())
        );
    }
}
