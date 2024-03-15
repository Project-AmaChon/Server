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

import java.util.List;
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
        addChildrenTechTags(cond);
        addChildrenRegionTags(cond);
        return projectSearchRepository.searchProjectByAllCond(cond);
    }

    private void addChildrenRegionTags(ProjectSearchCond cond) {
        for (String regionTag : cond.getRegionTagNames()) {
            cond.getRegionTagNames().addAll(regionTagService.getRegionTagNameWithChildrenTags(regionTag));
        }
    }

    private void addChildrenTechTags(ProjectSearchCond cond) {
        for (String techTag : cond.getTechTagNames()) {;
            cond.getTechTagNames().addAll(techTagService.getTechTagNameWithChildrenTags(techTag));
        }
    }
}
