package v1.amachon.domain.project.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.dto.ProjectDetailDto;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.ProjectImage;
import v1.amachon.domain.project.entity.RecruitManagement;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.repository.RecruitManagementRepository;
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
  private final RecruitManagementRepository recruitManagementRepository;

  @Transactional
  public void createProject(ProjectCreateRequestDto projectCreateDto) throws BaseException {
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
  }

  @Transactional
  public ProjectDetailDto getProjectDetailDto(Long id) throws BaseException {
    Project project = projectRepository.findById(id)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.PROJECT_NOT_FOUND));
    return ProjectDetailDto.builder()
        .id(project.getId())
        .title(project.getTitle())
        .description(project.getDescription())
        .recruitDeadline(project.getRecruitDeadline())
        .recruitNumber(project.getRecruitNumber())
        .developPeriod(project.getDevelopPeriod())
        .leaderId(project.getLeader().getId())
        .regionTagId(project.getRegionTag().getId())
        .techTagIds(project.getTechTags().stream()
            .map(ProjectTechTag::getId)
            .collect(Collectors.toList()))
        .imageUrls(project.getImages().stream()
            .map(ProjectImage::getImageUrl)
            .collect(Collectors.toList()))
        .teamMembers(getProjectTeamMembers(project.getId()))
        .build();
  }

  @Transactional
  public void deleteAllProjects() {
    projectRepository.deleteAll();
  }

  // 프로젝트 팀의 구성원 목록을 가져옴
  public List<Member> getProjectTeamMembers(Long projectId) {
    List<Member> teamMembers = new ArrayList<>();
    // 프로젝트 ID에 해당하는 RecruitManagement 엔티티 목록 가져오기
    List<RecruitManagement> recruitManagements = recruitManagementRepository.findByProjectId(projectId);
    // RecruitManagement 엔티티에 있는 회원 엔티티를 순회하며 가져와서 팀원 목록에 추가
    for (RecruitManagement recruitManagement : recruitManagements) {
      teamMembers.add(recruitManagement.getMember());
    }
    return teamMembers;
  }
}
