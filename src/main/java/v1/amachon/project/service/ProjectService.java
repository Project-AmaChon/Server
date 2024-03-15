package v1.amachon.project.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.project.repository.*;
import v1.amachon.project.service.request.*;
import v1.amachon.project.entity.Project;
import v1.amachon.project.service.exception.*;
import v1.amachon.project.service.response.ModifyProjectResponse;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.ProjectTechTagRepository;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;
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

    public void createProject(CreateProjectRequest createProjectRequest) {
        Member currentMember = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        RegionTag regionTag = regionTagRepository.findByName(createProjectRequest.getRegionTagName())
                .orElseThrow(NotFoundRegionTagException::new);
        Project project = Project.builder().title(createProjectRequest.getTitle()).description(createProjectRequest.getDescription())
                .recruitDeadline(createProjectRequest.getRecruitDeadline()).recruitNumber(createProjectRequest.getRecruitNumber())
                .developPeriod(createProjectRequest.getDevelopPeriod()).leader(currentMember).regionTag(regionTag)
                .build();
        projectRepository.save(project);
        saveTechTags(project, createProjectRequest.getTechTagNames());
    }

    public void saveTechTags(Project project, List<String> techTagNames) {
        for (String tagName : techTagNames) {
            TechTag techTag = techTagRepository.findByName(tagName)
                    .orElseThrow(NotFoundTechTagException::new);
            projectTechTagRepository.save(new ProjectTechTag(project, techTag));
        }
    }

    public ModifyProjectResponse getModifyProject(Long projectId) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        verifyProjectOwner(project);
        return new ModifyProjectResponse(project);
    }

    private void verifyProjectOwner(Project project) {
        Member currentMember = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        if (currentMember.getId().equals(project.getLeader().getId())) {
            throw new FailureProjectModifyException();
        }
    }

    public void modifyProject(Long projectId, ModifyProjectRequest modifyProjectRequest) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        RegionTag regionTag = regionTagRepository.findByName(modifyProjectRequest.getRegionTagName())
                .orElseThrow(NotFoundRegionTagException::new);

        verifyProjectOwner(project);

        project.modifyProject(modifyProjectRequest, regionTag);
        saveTechTags(project, modifyProjectRequest.getTechTagNames());
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        verifyProjectOwner(project);

        project.delete();
   }

    @Transactional(readOnly = true)
    public ProjectManagementDto getProjectManagement() {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        List<MyProjectDto> myProject = projectRepository.findByLeaderIdFetchTechTags(member.getId()).stream().map(MyProjectDto::new).collect(Collectors.toList());
        List<ParticipatingProjectDto> participatingProject = projectRepository.findParticipatingProjectByMemberId(member.getId()).stream().map(ParticipatingProjectDto::new).collect(Collectors.toList());
        return new ProjectManagementDto(participatingProject, myProject);
    }
}
