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
import v1.amachon.domain.member.dto.RecommendCond;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRecommendRepo;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.dto.ProjectDetailDto;
import v1.amachon.domain.project.dto.ProjectDto;
import v1.amachon.domain.project.dto.ProjectSearchCond;
import v1.amachon.domain.project.dto.recruit.RecruitManagementDto;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.ProjectImage;
import v1.amachon.domain.project.entity.RecruitManagement;
import v1.amachon.domain.project.entity.TeamMember;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.repository.ProjectSearchRepository;
import v1.amachon.domain.project.repository.RecruitManagementRepository;
import v1.amachon.domain.project.repository.TeamMemberRepository;
import v1.amachon.domain.tags.dto.RegionTagDto;
import v1.amachon.domain.tags.dto.TechTagDto;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.ProjectTechTagRepository;
import v1.amachon.domain.tags.repository.RegionTagRepository;
import v1.amachon.domain.tags.repository.TechTagRepository;
import v1.amachon.domain.tags.service.RegionTagService;
import v1.amachon.domain.tags.service.TechTagService;
import v1.amachon.global.config.security.util.SecurityUtils;

import java.util.stream.Collectors;

import static v1.amachon.domain.base.BaseResponseStatus.*;

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
    private final RegionTagService regionTagService;
    private final TechTagService techTagService;
    private final MemberRecommendRepo memberRecommendRepo;
    private final TeamMemberRepository teamMemberRepository;

    public void createProject(ProjectCreateRequestDto projectCreateDto) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));

        Project project = Project.builder()
                .title(projectCreateDto.getTitle())
                .description(projectCreateDto.getDescription())
                .recruitDeadline(projectCreateDto.getRecruitDeadline())
                .recruitNumber(projectCreateDto.getRecruitNumber())
                .developPeriod(projectCreateDto.getDevelopPeriod())
                .leader(member)
                .regionTag(member.getRegionTag())
                .build();

        for (String tagName : projectCreateDto.getTechTagNames()) {
            TechTag techTag = techTagRepository.findByName(tagName)
                    .orElseThrow(() -> new BaseException(POST_PROJECT_EMPTY_TECHTAG));
            ProjectTechTag projectTechTag = new ProjectTechTag(project, techTag);
            project.addTechTag(projectTechTag);
        }

//        // 이미지 정보를 저장
//        List<ProjectImage> images = projectCreateDto.getImageUrls().stream()
//                .map(url -> new ProjectImage(url, project))
//                .collect(Collectors.toList());
//        project.setImages(images);

        // 변경사항을 저장
        projectRepository.save(project);
    }

    public ProjectDetailDto getProjectDetailDto(Long id) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        return new ProjectDetailDto(project);
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

    public List<ProjectDto> getRecentProjects() {
        Page<Project> page = projectRepository.searchRecentProjects(PageRequest.of(0, 10));
        List<Project> projects = page.getContent();
        List<ProjectDto> converted = projects.stream().map(ProjectDto::new).collect(Collectors.toList());
        return converted;
    }

    public List<ProjectDto> getSearchProjects(ProjectSearchCond cond) throws BaseException {
        addChildren(cond);
        return projectSearchRepository.searchProjectByAllCond(cond);
    }

    public void addChildren(ProjectSearchCond cond) throws BaseException {
        for (String regionTag : cond.getRegionTagNames()) {
            RegionTagDto region = regionTagService.getRegionTag(regionTag);
            if (!region.getChildren().isEmpty()) {
                for (String childRegion : region.getChildren()) {
                    cond.regionTagNames.add(childRegion);
                }
            }
        }
        for (String techTag : cond.getTechTagNames()) {
            TechTagDto tech = techTagService.getTechTag(techTag);
            if (!tech.getChildren().isEmpty()) {
                for (String childTech : tech.getChildren()) {
                    cond.techTagNames.add(childTech);
                }
            }
        }
    }

    public void projectApply(Long projectId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new BaseException(BAD_REQUEST));
        List<Long> teamMemberIds = project.getTeamMembers().stream().map(t -> t.getMember().getId()).collect(Collectors.toList());
        if (project.getLeader().getId() == member.getId()) {
            throw new BaseException(BAD_REQUEST);
        } else if (teamMemberIds.contains(member.getId())) {
            throw new BaseException(PROJECT_APPLY_DENIED);
        }
        recruitManagementRepository.save(new RecruitManagement(member, project));
    }

    public List<RecruitManagementDto> getRecruitList(Long projectId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new BaseException(PROJECT_NOT_FOUND));
        if (project.getLeader().getId() != member.getId()) {
            throw new BaseException(INVALID_USER);
        }
        return project.getRecruitManagements().stream().map(r -> new RecruitManagementDto(r.getMember())).collect(Collectors.toList());
    }

    public List<RecruitManagementDto> getRecommendMember(Long projectId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new BaseException(PROJECT_NOT_FOUND));
        if (project.getLeader().getId() != member.getId()) {
            throw new BaseException(INVALID_USER);
        }
        RecommendCond cond = new RecommendCond(project);
        for (String regionTag : cond.getRegionTagNames()) {
            RegionTagDto region = regionTagService.getRegionTag(regionTag);
            if (!region.getChildren().isEmpty()) {
                for (String childRegion : region.getChildren()) {
                    cond.regionTagNames.add(childRegion);
                }
            }
        }
        return memberRecommendRepo.getRecommendMemberByCond(cond);
    }

    public void recruitAccept(Long recruitManagementId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        RecruitManagement recruitManagement = recruitManagementRepository.findById(recruitManagementId).orElseThrow(
                () -> new BaseException(NOT_FOUND_RECRUIT_MANAGEMENT));
        if (recruitManagement.getProject().getLeader().getId() != member.getId()) {
            throw new BaseException(INVALID_USER);
        }
        teamMemberRepository.save(new TeamMember(recruitManagement.getProject(), recruitManagement.getMember()));
        recruitManagement.expired();
    }

    public void recruitReject(Long recruitManagementId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        RecruitManagement recruitManagement = recruitManagementRepository.findById(recruitManagementId).orElseThrow(
                () -> new BaseException(NOT_FOUND_RECRUIT_MANAGEMENT));
        if (recruitManagement.getProject().getLeader().getId() != member.getId()) {
            throw new BaseException(INVALID_USER);
        }
        recruitManagement.expired();
    }
}
