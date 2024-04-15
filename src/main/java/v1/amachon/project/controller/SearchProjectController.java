package v1.amachon.project.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.service.SearchProjectService;
import v1.amachon.project.service.response.ProjectDetailResponse;
import v1.amachon.project.service.response.ProjectResponse;
import v1.amachon.project.service.request.ProjectSearchCond;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"프로젝트 조회 API"})
public class SearchProjectController {

    private final SearchProjectService searchProjectService;
    private final ProjectRepository projectRepository;

    @ApiOperation(value = "프로젝트 상세 조회", notes = "프로젝트 ID를 입력받아 해당 프로젝트의 상세 정보를 조회합니다.")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProjectDto(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(searchProjectService.getProjectDetail(projectId));
    }

    @ApiOperation(value = "프로젝트 다중 조건 검색", notes = "키워드, 지역, 기술 태그를 입력받아 검색된 프로젝트를 반환")
    @PostMapping("/project/search")
    public ResponseEntity<List<ProjectResponse>> getSearchProjects(@RequestBody ProjectSearchCond cond) {
        return ResponseEntity.ok(searchProjectService.getSearchProjects(cond));
    }

    @ApiOperation(value = "최신 프로젝트 페이지(Home)", notes = "Home 페이지로 최신 프로젝트 순으로 10개 반환")
    @GetMapping("/home")
    public ResponseEntity<List<ProjectResponse>> getRecentProjects()  {
        return ResponseEntity.ok(searchProjectService.getRecentProjects());
    }
}
