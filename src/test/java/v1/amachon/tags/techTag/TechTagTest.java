package v1.amachon.tags.techTag;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.entity.Project;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.tags.entity.techtag.MemberTechTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.MemberTechTagRepository;
import v1.amachon.tags.repository.ProjectTechTagRepository;
import v1.amachon.tags.repository.TechTagRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TechTagTest {

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTechTagRepository memberTechTagRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectTechTagRepository projectTechTagRepository;

    @Test
    @DisplayName("태그 생성")
    public void createTechTag() {
        TechTag backend = TechTag.builder().depth(0).name("Backend").build();
        TechTag frontend = TechTag.builder().depth(0).name("Frontend").build();
        TechTag spring = TechTag.builder().depth(1).name("Spring").parent(backend).build();
        TechTag nodeJS = TechTag.builder().depth(1).name("NodeJS").parent(backend).build();
        TechTag reactJS = TechTag.builder().depth(1).name("ReactJS").parent(frontend).build();

        assertThat(backend.getChildren().size()).isEqualTo(2);
        assertThat(frontend.getChildren().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("멤버에 기술 태그 저장")
    public void insertTagToMember() {
        Member member = Member.builder().email("acg6138").password("acg").nickname("이정우").build();
        TechTag spring = TechTag.builder().depth(1).name("spring").build();

        techTagRepository.save(spring);
        memberRepository.save(member);
        memberTechTagRepository.save(new MemberTechTag(member, spring));
    }

    @Test
    @DisplayName("프로젝트에 기술 태그 저장")
    public void insertTagToProject() {
        Project project = Project.builder().title("Amachon").description("아마추어를 위한 프로젝트 매칭 서비스").recruitDeadline(LocalDate.now())
                .recruitNumber(4).developPeriod(LocalDate.now()).build();
        TechTag spring = TechTag.builder().depth(1).name("spring").build();

        techTagRepository.save(spring);
        projectRepository.save(project);
        projectTechTagRepository.save(new ProjectTechTag(project, spring));

    }
}