package v1.amachon.project.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.common.config.s3.S3UploadUtil;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.member.service.dto.RecommendCond;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRecommendRepo;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.domain.project.service.dto.project.*;
import v1.amachon.project.repository.*;
import v1.amachon.project.service.dto.project.*;
import v1.amachon.project.service.dto.recruit.RecruitManagementDto;
import v1.amachon.project.entity.Project;
import v1.amachon.project.entity.ProjectImage;
import v1.amachon.project.entity.RecruitManagement;
import v1.amachon.project.entity.TeamMember;
import v1.amachon.domain.project.repository.*;
import v1.amachon.domain.project.service.exception.*;
import v1.amachon.project.service.exception.*;
import v1.amachon.tags.service.dto.RegionTagDto;
import v1.amachon.tags.service.dto.TechTagDto;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.ProjectTechTagRepository;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;
import v1.amachon.tags.service.RegionTagService;
import v1.amachon.tags.service.TechTagService;
import v1.amachon.tags.service.exception.NotFoundRegionTagException;
import v1.amachon.tags.service.exception.NotFoundTechTagException;
import v1.amachon.common.config.security.util.SecurityUtils;

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
    private final RegionTagService regionTagService;
    private final TechTagService techTagService;
    private final MemberRecommendRepo memberRecommendRepo;
    private final TeamMemberRepository teamMemberRepository;
    private final S3UploadUtil s3UploadUtil;
    private final ProjectImageRepository projectImageRepository;

    public void createProject(ProjectCreateRequestDto projectCreateDto) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        RegionTag regionTag = regionTagRepository.findByName(projectCreateDto.getRegionTagName())
                .orElseThrow(NotFoundRegionTagException::new);
        Project project = Project.builder()
                .title(projectCreateDto.getTitle())
                .description(projectCreateDto.getDescription())
                .recruitDeadline(projectCreateDto.getRecruitDeadline())
                .recruitNumber(projectCreateDto.getRecruitNumber())
                .developPeriod(projectCreateDto.getDevelopPeriod())
                .leader(member)
                .regionTag(regionTag)
                .build();
        projectRepository.save(project);
        List<ProjectTechTag> projectTechTags = new ArrayList<>();

        for (String tagName : projectCreateDto.getTechTagNames()) {
            TechTag techTag = techTagRepository.findByName(tagName)
                    .orElseThrow(NotFoundTechTagException::new);
            ProjectTechTag projectTechTag = projectTechTagRepository.save(new ProjectTechTag(project, techTag));
            projectTechTags.add(projectTechTag);
        }
        project.changeTechTag(projectTechTags);
        projectRepository.save(project);
    }

    public ProjectModifyDto getModifyProject(Long projectId) {
        Member leader = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        if (leader.getId() != project.getLeader().getId()) {
            throw new FailureProjectModifyException();
        }
        return new ProjectModifyDto(project);
    }

    public void modifyProject(Long projectId, ProjectModifyDto projectModifyDto, List<MultipartFile> images) {
        Member leader = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        if (leader.getId() != project.getLeader().getId()) {
            throw new FailureProjectModifyException();
        }
        List<ProjectTechTag> projectTechTags = new ArrayList<>();
        for (String techTag : projectModifyDto.getTechTagNames()) {
            ProjectTechTag projectTechTag = new ProjectTechTag(project,
                    techTagRepository.findByName(techTag).orElseThrow(NotFoundTechTagException::new));
            projectTechTags.add(projectTechTag);
        }
        RegionTag regionTag = regionTagRepository.findByName(projectModifyDto.getRegionTagName())
                .orElseThrow(NotFoundRegionTagException::new);

        deletePreviousImages(project);

        List<ProjectImage> projectImages = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for(MultipartFile image : images) {
                String imageUrl = s3UploadUtil.upload(image);
                projectImages.add(projectImageRepository.save(new ProjectImage(imageUrl, project)));
            }
        }
        project.modifyProject(projectModifyDto, projectTechTags, projectImages, regionTag);
        projectRepository.save(project);
    }

    private void deletePreviousImages(Project project) {
        if (!project.getImages().isEmpty()) {
            for(ProjectImage image : project.getImages()) {
                s3UploadUtil.fileDelete(image.getImageUrl());
            }
        }
    }

    public void deleteProject(Long projectId) {
        Member leader = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        if (leader.getId() != project.getLeader().getId()) {
            throw new BadRequestException();
        }
        for (RecruitManagement recruitManagement : project.getRecruitManagements()) {
            recruitManagement.expired();
        }
        for (TeamMember teamMember : project.getTeamMembers()) {
            teamMember.expired();
        }
        for (ProjectTechTag techTag : project.getTechTags()) {
            techTag.expired();
        }
        for (ProjectImage image : project.getImages()) {
            image.expired();
        }
        project.expired();
        projectRepository.save(project);
    }

    public ProjectDetailDto getProjectDetailDto(Long id) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.findByIdFetch(id)
                .orElseThrow(NotFoundProjectException::new);
        List<TeamMember> team = teamMemberRepository.findByProjectId(id);

        return new ProjectDetailDto(project, team);
    }

    public List<ProjectDto> getRecentProjects() {
        memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Page<Project> page = projectRepository.searchRecentProjects(PageRequest.of(0, 10));
        List<Project> projects = page.getContent();
        List<ProjectDto> converted = projects.stream().map(ProjectDto::new).collect(Collectors.toList());
        return converted;
    }

    public List<ProjectDto> getSearchProjects(ProjectSearchCond cond) {
        memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        addChildrenTags(cond);
        return projectSearchRepository.searchProjectByAllCond(cond);
    }

    private void addChildrenTags(ProjectSearchCond cond) {
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

    public void projectApply(Long projectId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(NotFoundProjectException::new);
        List<Long> teamMemberIds = project.getTeamMembers().stream().map(t -> t.getMember().getId()).collect(Collectors.toList());
        List<Long> recruitMemberIds = recruitManagementRepository.findByProjectId(projectId)
                .stream().map(r -> r.getMember().getId()).collect(Collectors.toList());
        if (project.getLeader().getId() == member.getId()) {
            throw new BadRequestException();
        } else if (teamMemberIds.contains(member.getId()) || recruitMemberIds.contains(member.getId())) {
            throw new ProjectApplyDeniedException();
        }
        recruitManagementRepository.save(new RecruitManagement(member, project));
    }

    public List<RecruitManagementDto> getRecruitList(Long projectId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.getRecruitListFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        if (project.getLeader().getId() != member.getId()) {
            throw new BadRequestException();
        }
        return project.getRecruitManagements().stream()
                .map(r -> new RecruitManagementDto(r.getMember(), r.getId()))
                .collect(Collectors.toList());
    }

    public List<RecruitManagementDto> getRecommendMember(Long projectId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        if (project.getLeader().getId() != member.getId()) {
            throw new BadRequestException();
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

    public void recruitAccept(Long recruitManagementId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        RecruitManagement recruitManagement = recruitManagementRepository.findByIdFetch(recruitManagementId)
                .orElseThrow(NotFoundRecruitManagementException::new);
        if (recruitManagement.getProject().getLeader().getId() != member.getId()) {
            throw new BadRequestException();
        }
        teamMemberRepository.save(new TeamMember(recruitManagement.getProject(), recruitManagement.getMember()));
        recruitManagement.expired();
    }

    public void recruitReject(Long recruitManagementId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        RecruitManagement recruitManagement = recruitManagementRepository.findByIdFetch(recruitManagementId)
                .orElseThrow(NotFoundRecruitManagementException::new);
        if (recruitManagement.getProject().getLeader().getId() != member.getId()) {
            throw new BadRequestException();
        }
        recruitManagement.expired();
    }

    public ProjectManagementDto getProjectManagement() {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        List<MyProjectDto> myProject = projectRepository.findByLeaderId(member.getId()).stream().map(MyProjectDto::new).collect(Collectors.toList());
        List<ParticipatingProjectDto> participatingProject = projectRepository.findByMemberId(member.getId()).stream().map(ParticipatingProjectDto::new).collect(Collectors.toList());
        return new ProjectManagementDto(participatingProject, myProject);
    }

    public void kickTeamMember(Long teamMemberId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(NotFoundTeamMemberException::new);
        if (member.getId() != teamMember.getProject().getLeader().getId()) {
            throw new BadRequestException();
        }
        teamMember.expired();
        teamMemberRepository.save(teamMember);
    }
}
