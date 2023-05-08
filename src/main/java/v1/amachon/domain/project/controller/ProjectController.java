package v1.amachon.domain.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.dto.ProjectCreateResponseDto;
import v1.amachon.domain.project.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping
  public ResponseEntity<ProjectCreateResponseDto> createProject(@RequestBody ProjectCreateRequestDto projectCreateDto) {
    ProjectCreateResponseDto createdProject = projectService.createProject(projectCreateDto);
    return ResponseEntity.ok(createdProject);
  }
}