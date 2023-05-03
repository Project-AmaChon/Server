package v1.amachon.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.base.BaseEntity;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.TeamMember;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.repository.TeamMemberRepository;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class BaseEntityTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    public void statusTest() {
        Member member = Member.builder().email("1").build();
        memberRepository.save(member);

        Project project = Project.builder().leader(member).build();
        projectRepository.save(project);

        TeamMember teamMember = new TeamMember(project, member);
        teamMemberRepository.save(teamMember);

        assertThat(member.getStatus()).isEqualTo(BaseEntity.Status.NORMAL);
        member.expired();
        assertThat(member.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED);
        assertThat(project.getStatus()).isEqualTo(BaseEntity.Status.NORMAL);
        project.expired();
        assertThat(project.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED);
        assertThat(teamMember.getStatus()).isEqualTo(BaseEntity.Status.NORMAL);
        teamMember.expired();
        assertThat(teamMember.getStatus()).isEqualTo(BaseEntity.Status.EXPIRED);
    }

    @Test
    public void dateTest() {
        Member member = Member.builder()
                .email("test")
                .build();
        memberRepository.save(member);
        assertThat(member.getCreatedDate()).isNotNull();
        assertThat(member.getLastModifiedDate()).isNotNull();
    }
}