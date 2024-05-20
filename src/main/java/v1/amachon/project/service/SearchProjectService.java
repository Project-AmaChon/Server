package v1.amachon.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.project.entity.Project;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.repository.ProjectSearchRepository;
import v1.amachon.project.service.response.ProjectDetailResponse;
import v1.amachon.project.service.response.ProjectResponse;
import v1.amachon.project.service.request.ProjectSearchCond;
import v1.amachon.project.service.exception.NotFoundProjectException;
import v1.amachon.tags.service.RegionTagService;
import v1.amachon.tags.service.TechTagService;
import v1.amachon.tags.service.dto.RegionTagDto;
import v1.amachon.tags.service.dto.TechTagDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SearchProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSearchRepository projectSearchRepository;
    private final RegionTagService regionTagService;
    private final TechTagService techTagService;

    public ProjectDetailResponse getProjectDetail(Long projectId) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);
        return new ProjectDetailResponse(project);
    }

    public List<ProjectResponse> getRecentProjects() {
        Page<Project> page = projectRepository.searchRecentProjects(PageRequest.of(0, 10));
        List<Project> projects = page.getContent();

        return projects.stream()
                .map(ProjectResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> getSearchProjects(ProjectSearchCond cond) {
        addChildrenRegionTags(cond);
        addChildrenTechTags(cond);
        return projectSearchRepository.searchProjectByAllCond(cond);
    }


    private void addChildrenRegionTags(ProjectSearchCond cond) {
        Set<String> tagNames = new HashSet<>();

        for (String regionTag : cond.getRegionTagNames()) {
            tagNames.addAll(regionTagService.getRegionTagNameWithChildrenTags(regionTag));
        }
        cond.regionTagNames.addAll(tagNames);
    }

    private void addChildrenTechTags(ProjectSearchCond cond) {
        Set<String> tagNames = new HashSet<>();

        for (String techTag : cond.getTechTagNames()) {;
            tagNames.addAll(techTagService.getTechTagNameWithChildrenTags(techTag));
        }
        cond.techTagNames.addAll(tagNames);
    }
}
