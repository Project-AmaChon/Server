package v1.amachon.domain.tags.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.repository.RegionTagRepository;

@SpringBootTest
class RegionTagTest {

    @Autowired
    private RegionTagRepository regionTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void createRegionTagTest() {
        RegionTag hwaseong = RegionTag.builder().depth(0).name("화성시").build();
        regionTagRepository.save(hwaseong);
    }

    @Test
    public void insertRegionTagToMember() {
        RegionTag hwaseong = RegionTag.builder().depth(0).name("화성시").build();
        regionTagRepository.save(hwaseong);
        Member member = Member.builder().email("d").build();
        member.changeRegion(hwaseong);
        memberRepository.save(member);
    }

    @Test
    public void insertRegionTagToProject() {
        RegionTag hwaseong = RegionTag.builder().depth(0).name("화성시").build();
        regionTagRepository.save(hwaseong);

        Member member = Member.builder().email("d").build();
        member.changeRegion(hwaseong);
        memberRepository.save(member);

        Project project = Project.builder().leader(member).regionTag(member.getRegionTag()).build();
        projectRepository.save(project);
    }
}