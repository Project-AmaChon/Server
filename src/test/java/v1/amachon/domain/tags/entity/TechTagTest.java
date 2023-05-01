package v1.amachon.domain.tags.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.tags.entity.techtag.MemberTechTag;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.MemberTechTagRepository;
import v1.amachon.domain.tags.repository.ProjectTechTagRepository;
import v1.amachon.domain.tags.repository.TechTagRepository;

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
    public void insertTagToMember() {
        Member member = Member.builder().email("acg6138").password("acg").nickname("이정우").build();
        TechTag spring = TechTag.builder().depth(1).name("spring").build();
        techTagRepository.save(spring);
        memberRepository.save(member);
        memberTechTagRepository.save(new MemberTechTag(member, spring));

        assertThat(member.getTechTags().get(0).getTechTag()).isEqualTo(spring);
    }

    @Test
    public void insertTagToProject() {
        Project project = Project.builder().title("Amachon").description("아마추어를 위한 프로젝트 매칭 서비스").recruitDeadline(LocalDate.now())
                .recruitNumber(4).developPeriod(LocalDate.now()).build();
        TechTag spring = TechTag.builder().depth(1).name("spring").build();
        techTagRepository.save(spring);
        projectRepository.save(project);
        projectTechTagRepository.save(new ProjectTechTag(project, spring));

        assertThat(project.getTechTags().get(0).getTechTag()).isEqualTo(spring);
    }
}