package v1.amachon.domain.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.TeamMember;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.repository.TeamMemberRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TeamMemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public static Project project;
    public static Member leader;
    public static Member follower1;
    public static Member follower2;
    public static Member follower3;
    public static Member follower4;

    @BeforeEach
    public void init() {
        leader = Member.builder().email("email-1").nickname("이정우").password("1").build();
        follower1 = Member.builder().email("email-2").nickname("허규범").password("1").build();
        follower2 = Member.builder().email("email-3").nickname("박종범").password("1").build();
        follower3 = Member.builder().email("email-4").nickname("전재욱").password("1").build();
        follower4 = Member.builder().email("email-5").nickname("전승현").password("1").build();
        project = Project.builder().title("Amachon").description("아마추어를 위한 프로젝트 매칭 서비스").recruitDeadline(LocalDate.now())
                .recruitNumber(4).developPeriod(LocalDate.now()).leader(leader).build();
        memberRepository.save(leader);
        memberRepository.save(follower1);
        memberRepository.save(follower2);
        memberRepository.save(follower3);
        memberRepository.save(follower4);
        projectRepository.save(project);
    }

    @Test
    @DisplayName("팀 멤버 생성")
    public void createTeamMemberTest() {
        project.addTeamMember(new TeamMember(project, follower1));
        project.addTeamMember(new TeamMember(project, follower2));
        project.addTeamMember(new TeamMember(project, follower3));
        project.addTeamMember(new TeamMember(project, follower4));

        projectRepository.save(project);

        assertThat(project.getTeamMembers().size()).isEqualTo(4);
    }
}