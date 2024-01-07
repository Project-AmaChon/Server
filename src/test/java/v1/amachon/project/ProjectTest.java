package v1.amachon.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.entity.Project;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.repository.ProjectSearchRepository;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.ProjectTechTagRepository;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;

@SpringBootTest
class ProjectTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private ProjectTechTagRepository projectTechTagRepository;

    @Autowired
    private ProjectSearchRepository projectSearchRepository;

    @Autowired
    private RegionTagRepository regionTagRepository;

    @Test
    @DisplayName("기술 태그가 많이 겹치는 순으로 프로젝트 조회")
    public void searchProjectTest() {
        // Member 생성(지역 태그 삽입)
        Member member = Member.builder().email("a").nickname("ac").build();
        RegionTag hwasung = RegionTag.builder().name("화성시").depth(1).build();
        regionTagRepository.save(hwasung);
        member.changeRegion(hwasung);
        memberRepository.save(member);

        // 기술 태그 3개 생성, project 생성(기술 태그 3개 삽입)
        TechTag spring = techTagRepository.save(TechTag.builder().name("spring").depth(1).build());
        TechTag nodejs = techTagRepository.save(TechTag.builder().name("nodejs").depth(1).build());
        TechTag django = techTagRepository.save(TechTag.builder().name("django").depth(1).build());
        Project project = Project.builder().leader(member).title("3개").regionTag(member.getRegionTag()).build();
        project.addTechTag(new ProjectTechTag(project, spring));
        project.addTechTag(new ProjectTechTag(project, nodejs));
        project.addTechTag(new ProjectTechTag(project, django));
        projectRepository.save(project);

        // 기술 태그 1개, 2개를 삽입한 project1, project2 생성
        Project project2 = Project.builder().leader(member).title("1개").regionTag(member.getRegionTag()).build();
        project2.addTechTag(new ProjectTechTag(project2, spring));
        Project project3 = Project.builder().leader(member).title("2개").regionTag(member.getRegionTag()).build();
        project3.addTechTag(new ProjectTechTag(project3, nodejs));
        project3.addTechTag(new ProjectTechTag(project3, spring));
        projectRepository.save(project2);
        projectRepository.save(project3);

        // 태그가 많은 순서대로 정렬한 프로젝트 리스트를 반환

    }
}