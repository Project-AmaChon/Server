package v1.amachon.project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.RecruitManagement;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.repository.RecruitManagementRepository;
import v1.amachon.domain.project.repository.TeamMemberRepository;

import java.time.LocalDate;

@SpringBootTest
public class RecruitManagementTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RecruitManagementRepository recruitManagementRepository;

    public static Project project;
    public static Member participant1;
    public static Member participant2;
    public static Member participant3;
    public static Member participant4;

    @BeforeEach
    public void init() {
        participant1 = Member.builder().email("email-2").nickname("허규범").password("1").build();
        participant2 = Member.builder().email("email-3").nickname("박종범").password("1").build();
        participant3 = Member.builder().email("email-4").nickname("전재욱").password("1").build();
        participant4 = Member.builder().email("email-5").nickname("전승현").password("1").build();
        project = Project.builder().title("Amachon").description("아마추어를 위한 프로젝트 매칭 서비스").recruitDeadline(LocalDate.now())
                .recruitNumber(4).developPeriod(LocalDate.now()).build();

        memberRepository.save(participant1);
        memberRepository.save(participant2);
        memberRepository.save(participant3);
        memberRepository.save(participant4);
        projectRepository.save(project);
    }

    @Test
    @DisplayName("참가자 관리 생성")
    public void createRecruitManagement() {
        recruitManagementRepository.save(new RecruitManagement(participant1, project));
        recruitManagementRepository.save(new RecruitManagement(participant2, project));
        recruitManagementRepository.save(new RecruitManagement(participant3, project));
        recruitManagementRepository.save(new RecruitManagement(participant4, project));

        Assertions.assertThat(project.getRecruitManagements().size()).isEqualTo(4);
    }
}
