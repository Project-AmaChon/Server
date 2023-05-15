package v1.amachon.domain.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.member.dto.join.JoinDto;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.entity.techtag.MemberTechTag;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.MemberTechTagRepository;
import v1.amachon.domain.tags.repository.ProjectTechTagRepository;
import v1.amachon.domain.tags.repository.RegionTagRepository;
import v1.amachon.domain.tags.repository.TechTagRepository;

import java.time.LocalDate;
import java.util.Arrays;


@RequiredArgsConstructor
@RestController
@Api(tags = "데이터 세팅 API")
public class BaseController {

    private final TechTagRepository techTagRepository;
    private final RegionTagRepository regionTagRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTechTagRepository projectTechTagRepository;
    private final MemberRepository memberRepository;
    private final MemberTechTagRepository memberTechTagRepository;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(
            value = "초기 데이터 삽입",
            notes = "태그, 프로젝트, 멤버 데이터 초기화"
    )
    @PostMapping("/init")
    public void init() throws BaseException {
        // 기술 태그
        TechTag backend = TechTag.builder().depth(0).name("Backend").build();
        TechTag frontend = TechTag.builder().depth(0).name("Frontend").build();
        techTagRepository.save(backend);
        techTagRepository.save(frontend);

        TechTag spring = TechTag.builder().depth(1).name("Spring").parent(backend).build();
        TechTag nodeJS = TechTag.builder().depth(1).name("NodeJS").parent(backend).build();
        TechTag reactJS = TechTag.builder().depth(1).name("ReactJS").parent(frontend).build();
        techTagRepository.save(spring);
        techTagRepository.save(nodeJS);
        techTagRepository.save(reactJS);

        // 지역 태그
        RegionTag 서울 = RegionTag.builder().depth(0).name("서울").build();
        regionTagRepository.save(서울);
        RegionTag 강남구 = RegionTag.builder().parent(서울).depth(1).name("강남구").build();
        RegionTag 강동구 = RegionTag.builder().parent(서울).depth(1).name("강동구").build();
        regionTagRepository.save(강남구);
        regionTagRepository.save(강동구);

        RegionTag 경기 = RegionTag.builder().depth(0).name("경기").build();
        regionTagRepository.save(경기);
        RegionTag 가평군 = RegionTag.builder().parent(경기).depth(1).name("가평군").build();
        RegionTag 용인시_수지구 = RegionTag.builder().parent(경기).depth(1).name("용인시 수지구").build();
        regionTagRepository.save(가평군);
        regionTagRepository.save(용인시_수지구);

        // 멤버
        Member 이정우 = Member.ofMember(JoinDto.builder().email("acg6138@naver.com").nickname("이정우").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 박종범 = Member.ofMember(JoinDto.builder().email("acg61381@naver.com").nickname("박종범").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 강현민 = Member.ofMember(JoinDto.builder().email("acg61382@naver.com").nickname("강현민").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 백승진 = Member.ofMember(JoinDto.builder().email("acg61383@naver.com").nickname("백승진").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 전재욱 = Member.ofMember(JoinDto.builder().email("acg61384@naver.com").nickname("전재욱").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 전승현 = Member.ofMember(JoinDto.builder().email("acg61385@naver.com").nickname("전승현").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 허규범 = Member.ofMember(JoinDto.builder().email("acg61386@naver.com").nickname("허규범").password(passwordEncoder.encode("wjddn6138!")).build());

        memberRepository.save(이정우);
        memberRepository.save(강현민);
        memberRepository.save(백승진);
        memberRepository.save(박종범);
        memberRepository.save(전재욱);
        memberRepository.save(전승현);
        memberRepository.save(허규범);

        MemberTechTag m1 = memberTechTagRepository.save(new MemberTechTag(이정우, spring));
        MemberTechTag m2 = memberTechTagRepository.save(new MemberTechTag(이정우, nodeJS));
        MemberTechTag m3 = memberTechTagRepository.save(new MemberTechTag(박종범, reactJS));
        MemberTechTag m4 = memberTechTagRepository.save(new MemberTechTag(강현민, reactJS));
        MemberTechTag m5 = memberTechTagRepository.save(new MemberTechTag(강현민, nodeJS));
        MemberTechTag m6 = memberTechTagRepository.save(new MemberTechTag(백승진, spring));
        MemberTechTag m7 = memberTechTagRepository.save(new MemberTechTag(전재욱, nodeJS));
        MemberTechTag m8 = memberTechTagRepository.save(new MemberTechTag(전승현, reactJS));
        MemberTechTag m9 = memberTechTagRepository.save(new MemberTechTag(허규범, spring));
        MemberTechTag m10 = memberTechTagRepository.save(new MemberTechTag(허규범, reactJS));

        이정우.changeRegion(강남구);
        이정우.changeTechTag(Arrays.asList(m1, m2));
        박종범.changeRegion(강남구);
        박종범.changeTechTag(Arrays.asList(m3));
        강현민.changeRegion(강남구);
        강현민.changeTechTag(Arrays.asList(m4, m5));
        백승진.changeRegion(가평군);
        백승진.changeTechTag(Arrays.asList(m6));
        전재욱.changeRegion(가평군);
        전재욱.changeTechTag(Arrays.asList(m7));
        전승현.changeRegion(강동구);
        전승현.changeTechTag(Arrays.asList(m8));
        허규범.changeRegion(강동구);
        허규범.changeTechTag(Arrays.asList(m9, m10));

        // 프로젝트
        Project project1= Project.builder().title("프로젝트").leader(이정우).regionTag(강남구).recruitDeadline(LocalDate.now()).recruitNumber(3).build();
        projectRepository.save(project1);
        projectTechTagRepository.save(new ProjectTechTag(project1, nodeJS));
        projectTechTagRepository.save(new ProjectTechTag(project1, spring));
        Project project2 = Project.builder().title("프로젝트").leader(이정우).regionTag(강동구).recruitDeadline(LocalDate.now()).recruitNumber(3).build();
        projectRepository.save(project2);
        projectTechTagRepository.save(new ProjectTechTag(project2, reactJS));
    }
}
