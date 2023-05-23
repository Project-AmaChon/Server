package v1.amachon.domain.project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.dto.project.ProjectCreateRequestDto;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.service.ProjectService;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.repository.RegionTagRepository;
import java.time.LocalDate;
import java.util.Arrays;
import v1.amachon.domain.tags.repository.TechTagRepository;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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
        .leaderId(leader.getId())
        .regionTagId(regionTag.getId())
        .techTagIds(techTagIds)
        .imageUrls(Arrays.asList("이미지1.jpg", "이미지2.jpg"))
        .build();
  }

  @AfterEach
  public void clearDB() {
    // 테스트에 사용된 데이터를 삭제
    projectService.deleteAllProjects();
    memberRepository.deleteAll();
    regionTagRepository.deleteAll();
    techTagRepository.deleteAll();
  }

  @Nested
  @DisplayName("성공하는 경우")
  class Success {

    @Test
    @DisplayName("정상적으로 프로젝트 생성")
    public void createProjectTest() throws BaseException {
      // Given
      long projectCountBeforeCreate = projectRepository.count();

      // When
      projectService.createProject(projectCreateDto);

      // Then
      Assertions.assertThat(projectRepository.count()).isEqualTo(projectCountBeforeCreate + 1);
//      assertEquals(projectCountBeforeCreate + 1, projectRepository.count(), "프로젝트 생성 실패");
    }
  }

  @Nested
  @DisplayName("실패하는 경우")
  class Fail {

    @Test
    @DisplayName("리더 정보가 잘못된 경우")
    public void createProjectFail_invalidLeader() throws BaseException {
      // Given
      projectCreateDto.setLeaderId(9999L);

      // When & Then
      assertThrows(BaseException.class, () -> projectService.createProject(projectCreateDto), "예외가 발생하지 않음");
    }

    @Test
    @DisplayName("지역 태그 정보가 잘못된 경우")
    public void createProjectFail_invalidRegionTag() throws BaseException {
      // Given
      projectCreateDto.setRegionTagId(9999L);

      // When & Then
      assertThrows(BaseException.class, () -> projectService.createProject(projectCreateDto), "예외가 발생하지 않음");
    }

    @Test
    @DisplayName("기술 태그 정보가 잘못된 경우")
    public void createProjectFail_invalidTechTags() throws BaseException {
      // Given
      projectCreateDto.setTechTagIds(Arrays.asList(9999L, 10000L));

      // When & Then
      assertThrows(BaseException.class, () -> projectService.createProject(projectCreateDto), "예외가 발생하지 않음");
    }
  }
}
