package v1.amachon.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.service.dto.project.ProjectCreateRequestDto;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.service.ProjectService;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.repository.RegionTagRepository;

import java.time.LocalDate;
import java.util.Arrays;
import v1.amachon.tags.repository.TechTagRepository;
import v1.amachon.tags.entity.techtag.TechTag;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@DisplayName("프로젝트 생성 테스트")
public class ProjectServiceTest {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private RegionTagRepository regionTagRepository;

  @Autowired
  private TechTagRepository techTagRepository;

  private ProjectCreateRequestDto projectCreateDto;


  @BeforeEach
  public void initDB() {
    Member leader = memberRepository.save(Member.builder()
        .email("leader@example.com")
        .nickname("리더닉네임")
        .password("password")
        .build());

    RegionTag regionTag = regionTagRepository.save(RegionTag.builder()
        .name("서울시")
        .depth(0)
        .parent(null)
        .build());

    List<TechTag> techTags = Arrays.asList(
        TechTag.builder().name("Java").build(),
        TechTag.builder().name("Spring").build(),
        TechTag.builder().name("React").build()
    );
    techTagRepository.saveAll(techTags);
    List<Long> techTagIds = techTags.stream().map(TechTag::getId).collect(Collectors.toList());

    projectCreateDto = ProjectCreateRequestDto.builder()
        .title("프로젝트 제목")
        .description("프로젝트 설명")
        .recruitDeadline(LocalDate.of(2023, 5, 20))
        .recruitNumber(5)
        .developPeriod(LocalDate.of(2023, 6, 30))
        .build();
  }
}
