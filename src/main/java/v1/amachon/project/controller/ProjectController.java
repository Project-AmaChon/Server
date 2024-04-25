package v1.amachon.project.controller;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v1.amachon.common.config.security.AuthenticatedMemberEmail;
import v1.amachon.project.service.request.CreateProjectRequest;
import v1.amachon.project.service.request.ModifyProjectRequest;
import v1.amachon.project.service.ProjectService;
import v1.amachon.project.service.response.ModifyProjectResponse;

@RequiredArgsConstructor
@RestController
@Api(tags = {"프로젝트 API"})
public class ProjectController {

    private final ProjectService projectService;

    @ApiOperation(value = "프로젝트 생성 페이지: 새 프로젝트 생성", notes = "모집인원수, 기간, 제목, 태그 등을 입력받아 해당 정보를 토대로 프로젝트 게시글 생성, 작성자는 리더로 위임")
    @PostMapping("/project")
    public ResponseEntity<Void> createProject(@RequestBody CreateProjectRequest projectCreateDto) {
        projectService.createProject(projectCreateDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "프로젝트 수정", notes = "프로젝트 수정 페이지 정보 받아오기")
    @GetMapping("/project/{projectId}/modify")
    public ResponseEntity<ModifyProjectResponse> getModifyProject(@Parameter(hidden = true) @AuthenticatedMemberEmail String email,
                                                                  @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.getModifyProject(projectId));
    }

    @ApiOperation(value = "프로젝트 수정", notes = "프로젝트 수정하기")
    @PatchMapping("/project/{projectId}/modify")
    public ResponseEntity<Void> modifyProject(@Parameter(hidden = true) @AuthenticatedMemberEmail String email, @PathVariable("projectId") Long projectId,
                                              @RequestBody ModifyProjectRequest modifyProjectRequest) {
        projectService.modifyProject(projectId, modifyProjectRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트 삭제하기(상태 값 변경(EXPIRED))")
    @PostMapping("/project/{projectId}/delete")
    public ResponseEntity<Void> deleteProject(@Parameter(hidden = true) @AuthenticatedMemberEmail String email, @PathVariable("projectId") Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
}
