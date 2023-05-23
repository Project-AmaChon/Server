package v1.amachon.domain.project.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.member.dto.RecommendCond;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRecommendRepo;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.dto.project.*;
import v1.amachon.domain.project.dto.recruit.RecruitManagementDto;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.ProjectImage;
import v1.amachon.domain.project.entity.RecruitManagement;
import v1.amachon.domain.project.entity.TeamMember;
import v1.amachon.domain.project.repository.*;
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
import v1.amachon.global.config.s3.S3UploadUtil;
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
    private final S3UploadUtil s3UploadUtil;
    private final ProjectImageRepository projectImageRepository;

    public void createProject(ProjectCreateRequestDto projectCreateDto, List<MultipartFile> images) throws BaseException, IOException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        RegionTag regionTag = regionTagRepository.findByName(projectCreateDto.getRegionTagName()).orElseThrow(
                () -> new BaseException(POST_PROJECT_EMPTY_REGIONTAG));
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
                    .orElseThrow(() -> new BaseException(POST_PROJECT_EMPTY_TECHTAG));
            ProjectTechTag projectTechTag = projectTechTagRepository.save(new ProjectTechTag(project, techTag));
            projectTechTags.add(projectTechTag);
        }
        project.changeTechTag(projectTechTags);

        for(MultipartFile image : images) {
            String imageUrl = s3UploadUtil.upload(image);
            projectImageRepository.save(new ProjectImage(imageUrl, project));
        }
        projectRepository.save(project);
    }

    public ProjectModifyDto getModifyProject(Long projectId) throws BaseException {
        memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(INVALID_USER));
        Project project = projectRepository.findByIdFetch(projectId).orElseThrow(
                () -> new BaseException(PROJECT_NOT_FOUND));
        return new ProjectModifyDto(project);
    }

    public void modifyProject(Long projectId, ProjectModifyDto projectModifyDto, List<MultipartFile> images) throws BaseException, IOException {
        memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(INVALID_USER));
        Project project = projectRepository.findByIdFetch(projectId).orElseThrow(
                () -> new BaseException(PROJECT_NOT_FOUND));

        List<ProjectTechTag> projectTechTags = new ArrayList<>();
        for (String t : projectModifyDto.getTechTagNames()) {
            ProjectTechTag projectTechTag = new ProjectTechTag(project, techTagRepository.findByName(t).orElseThrow(
                    () -> new BaseException(INVALID_TAG)));
            projectTechTags.add(projectTechTag);
        }
        RegionTag regionTag = regionTagRepository.findByName(projectModifyDto.getRegionTagName()).orElseThrow(
                () -> new BaseException(INVALID_TAG));

        // 기존 이미지를 s3에서 삭제
        if (!project.getImages().isEmpty()) {
            for(ProjectImage image : project.getImages()) {
                s3UploadUtil.fileDelete(image.getImageUrl());
            }
        }

        List<ProjectImage> projectImages = new ArrayList<>();
        for(MultipartFile image : images) {
            String imageUrl = s3UploadUtil.upload(image);
            projectImages.add(projectImageRepository.save(new ProjectImage(imageUrl, project)));
        }
        project.modifyProject(projectModifyDto, projectTechTags, projectImages, regionTag);
        projectRepository.save(project);
    }

    public void deleteProject(Long projectId) throws BaseException {
        Project project = projectRepository.findByIdFetch(projectId).orElseThrow(
                () -> new BaseException(PROJECT_NOT_FOUND));
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

    public ProjectDetailDto getProjectDetailDto(Long id) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Project project = projectRepository.findByIdFetch(id)
                .orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        List<TeamMember> team = teamMemberRepository.findByProjectId(id);

        return new ProjectDetailDto(project, team);
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
        List<Long> recruitMemberIds = recruitManagementRepository.findByProjectId(projectId)
                .stream().map(r -> r.getMember().getId()).collect(Collectors.toList());
        if (project.getLeader().getId() == member.getId()) {
            throw new BaseException(BAD_REQUEST);
        } else if (teamMemberIds.contains(member.getId())) {
            throw new BaseException(PROJECT_APPLY_DENIED);
        } else if (recruitMemberIds.contains(member.getId())) {
            throw new BaseException(ALREADY_APPLY);
        }
        recruitManagementRepository.save(new RecruitManagement(member, project));
    }

    public List<RecruitManagementDto> getRecruitList(Long projectId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Project project = projectRepository.getRecruitListFetch(projectId).orElseThrow(
                () -> new BaseException(PROJECT_NOT_FOUND));
        if (project.getLeader().getId() != member.getId()) {
            throw new BaseException(INVALID_USER);
        }
        return project.getRecruitManagements().stream()
                .map(r -> new RecruitManagementDto(r.getMember(), r.getId()))
                .collect(Collectors.toList());
    }

    public List<RecruitManagementDto> getRecommendMember(Long projectId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Project project = projectRepository.findByIdFetch(projectId).orElseThrow(
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
        RecruitManagement recruitManagement = recruitManagementRepository.findByIdFetch(recruitManagementId).orElseThrow(
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
        RecruitManagement recruitManagement = recruitManagementRepository.findByIdFetch(recruitManagementId).orElseThrow(
                () -> new BaseException(NOT_FOUND_RECRUIT_MANAGEMENT));
        if (recruitManagement.getProject().getLeader().getId() != member.getId()) {
            throw new BaseException(INVALID_USER);
        }
        recruitManagement.expired();
    }
}
