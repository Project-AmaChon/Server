package v1.amachon.project.service;

import java.util.ArrayList;
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
import v1.amachon.project.service.response.UpdateProjectResponse;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.repository.TechTagRepository;
import v1.amachon.tags.service.exception.NotFoundRegionTagException;
import v1.amachon.tags.service.exception.NotFoundTechTagException;
import v1.amachon.common.config.security.util.SecurityUtils;


@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TechTagRepository techTagRepository;
    private final RegionTagRepository regionTagRepository;

    public Long createProject(CreateProjectRequest createProjectRequest) {
        Member currentMember = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        RegionTag regionTag = regionTagRepository.findByName(createProjectRequest.getRegionTagName())
                .orElseThrow(NotFoundRegionTagException::new);
        Project project = Project.builder().title(createProjectRequest.getTitle()).description(createProjectRequest.getDescription())
                .recruitDeadline(createProjectRequest.getRecruitDeadline()).recruitNumber(createProjectRequest.getRecruitNumber())
                .developPeriod(createProjectRequest.getDevelopPeriod()).leader(currentMember).regionTag(regionTag)
                .build();
        project.updateTechTags(convertToProjectTechTags(project, createProjectRequest.getTechTagNames()));
        return projectRepository.save(project).getId();
    }

    public List<ProjectTechTag> convertToProjectTechTags(Project project, List<String> techTagNames) {
        List<ProjectTechTag> techTags = new ArrayList<>();
        for (String tagName : techTagNames) {
            TechTag techTag = techTagRepository.findByName(tagName)
                    .orElseThrow(NotFoundTechTagException::new);
            techTags.add(new ProjectTechTag(project, techTag));
        }
        return techTags;
    }

    public UpdateProjectResponse getUpdateProjectResponse(Long projectId) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        verifyProjectOwner(project);
        return new UpdateProjectResponse(project);
    }

    private void verifyProjectOwner(Project project) {
        Member currentMember = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        if (!currentMember.getId().equals(project.getLeader().getId())) {
            throw new FailureProjectModifyException();
        }
    }

    public void updateProject(Long projectId, UpdateProjectRequest updateProjectRequest) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        RegionTag regionTag = regionTagRepository.findByName(updateProjectRequest.getRegionTagName())
                .orElseThrow(NotFoundRegionTagException::new);

        verifyProjectOwner(project);

        project.updateProject(updateProjectRequest);
        project.updateRegionTag(regionTag);
        project.updateTechTags(convertToProjectTechTags(project, updateProjectRequest.getTechTagNames()));
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);

        verifyProjectOwner(project);

        project.delete();
   }

}
