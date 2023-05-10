package v1.amachon.domain.project.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.dto.ProjectCreateResponseDto;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.ProjectImage;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.RegionTagRepository;
import v1.amachon.domain.tags.repository.TechTagRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final MemberRepository memberRepository;
  private final TechTagRepository techTagRepository;
  private final RegionTagRepository regionTagRepository;

  @Transactional
  public ProjectCreateResponseDto createProject(ProjectCreateRequestDto projectCreateDto) throws BaseException {
    Project project = Project.builder()
        .title(projectCreateDto.getTitle())
        .description(projectCreateDto.getDescription())
        .recruitDeadline(projectCreateDto.getRecruitDeadline())
        .recruitNumber(projectCreateDto.getRecruitNumber())
        .developPeriod(projectCreateDto.getDevelopPeriod())
        .leader(memberRepository.findById(projectCreateDto.getLeaderId())
            .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_PROJECT_EMPTY_LEADER)))
        .regionTag(regionTagRepository.findById(projectCreateDto.getRegionTagId())
            .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_PROJECT_EMPTY_REGIONTAG)))
        .build();

    for (Long tagId : projectCreateDto.getTechTagIds()) {
      TechTag techTag = techTagRepository.findById(tagId)
          .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_PROJECT_EMPTY_TECHTAG));
      ProjectTechTag projectTechTag = new ProjectTechTag(project, techTag);
      project.addTechTag(projectTechTag);
    }

    // 프로젝트를 먼저 저장
    Project savedProject = projectRepository.save(project);

    // 이미지 정보를 저장
    List<ProjectImage> images = projectCreateDto.getImageUrls().stream()
        .map(url -> new ProjectImage(url, savedProject))
        .collect(Collectors.toList());
    savedProject.setImages(images);

    // 변경사항을 저장
    projectRepository.save(savedProject);

    return ProjectCreateResponseDto.builder()
        .id(savedProject.getId())
        .title(savedProject.getTitle())
        .description(savedProject.getDescription())
        .recruitDeadline(savedProject.getRecruitDeadline())
        .recruitNumber(savedProject.getRecruitNumber())
        .developPeriod(savedProject.getDevelopPeriod())
        .leaderId(savedProject.getLeader().getId())
        .regionTagId(savedProject.getRegionTag().getId())
        .techTags(savedProject.getTechTags().stream().map(ProjectTechTag::getTechTag).collect(Collectors.toList()))
        .imageUrls(savedProject.getImages().stream().map(ProjectImage::getImageUrl).collect(Collectors.toList()))
        .build();
  }

  @Transactional
  public void deleteAllProjects() {
    projectRepository.deleteAll();
  }

  // 여기에 추가적인 서비스 메서드 작성해야됨
}
