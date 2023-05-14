package v1.amachon.domain.project.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.dto.ProjectDetailDto;
import v1.amachon.domain.project.dto.ProjectDto;
import v1.amachon.domain.project.dto.ProjectSearchCond;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.ProjectImage;
import v1.amachon.domain.project.entity.RecruitManagement;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.repository.ProjectSearchRepository;
import v1.amachon.domain.project.repository.RecruitManagementRepository;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.ProjectTechTagRepository;
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
    private final ProjectTechTagRepository projectTechTagRepository;
    private final RecruitManagementRepository recruitManagementRepository;
    private final ProjectSearchRepository projectSearchRepository;

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

    public void init() {
        TechTag backend = TechTag.builder().depth(0).name("Backend").build();
        TechTag frontend = TechTag.builder().depth(0).name("Frontend").build();
        techTagRepository.save(backend);
        techTagRepository.save(frontend);

        // child
        TechTag spring = TechTag.builder().depth(1).name("Spring").parent(backend).build();
        TechTag nodeJS = TechTag.builder().depth(1).name("NodeJS").parent(backend).build();
        TechTag reactJS = TechTag.builder().depth(1).name("ReactJS").parent(frontend).build();
        techTagRepository.save(spring);
        techTagRepository.save(nodeJS);
        techTagRepository.save(reactJS);
        Project project1= Project.builder().title("프로젝트 ").recruitDeadline(LocalDate.now()).recruitNumber(3).build();
        ProjectTechTag save = projectTechTagRepository.save(new ProjectTechTag(project1, nodeJS));
        project1.addTechTag(save);
        projectRepository.save(project1);
        projectTechTagRepository.save(new ProjectTechTag(project1, reactJS));
        Project project2 = Project.builder().title("프로젝트 ").recruitDeadline(LocalDate.now()).recruitNumber(3).build();
        projectRepository.save(project2);
        projectTechTagRepository.save(new ProjectTechTag(project2, reactJS));
        projectTechTagRepository.save(new ProjectTechTag(project2, nodeJS));
        projectTechTagRepository.save(new ProjectTechTag(project2, spring));
        for (int i = 0; i < 10; i++) {
            Project project = Project.builder().title("프로젝트 " + i).recruitDeadline(LocalDate.now()).recruitNumber(3).build();
            ProjectTechTag tag = projectTechTagRepository.save(new ProjectTechTag(project, spring));
            project.addTechTag(tag);
            projectRepository.save(project);
        }
    }

    public List<ProjectDto> getRecentProjects() {
        Page<Project> page = projectRepository.searchRecentProjects(PageRequest.of(0, 10));
        List<Project> projects = page.getContent();
        List<ProjectDto> converted = projects.stream().map(ProjectDto::new).collect(Collectors.toList());
        return converted;
    }

    public List<ProjectDto> getSearchProjects(ProjectSearchCond cond, int page) {
        return projectSearchRepository.searchProjectByAllCond(cond, page);
    }
}
