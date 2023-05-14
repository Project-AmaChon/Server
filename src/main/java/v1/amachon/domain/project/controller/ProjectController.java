package v1.amachon.domain.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.dto.ProjectDetailDto;
import v1.amachon.domain.project.dto.ProjectDto;
import v1.amachon.domain.project.dto.ProjectSearchCond;
import v1.amachon.domain.project.service.ProjectService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"프로젝트 API"})
public class ProjectController {

    private final ProjectService projectService;

    @ApiOperation(
            value = "프로젝트 생성 페이지: 새 프로젝트 생성",
            notes = "모집인원수, 기간, 제목, 태그 등을 입력받아 해당 정보를 토대로 프로젝트 게시글 생성, 작성자는 리더로 위임"
    )
    @ApiResponses({
            @ApiResponse(code = 2000, message = "Request error, 입력값을 확인해주세요."),
    })
    @PostMapping("/project")
    public BaseResponse<String> createProject(@RequestBody ProjectCreateRequestDto projectCreateDto) {
        try {
            projectService.createProject(projectCreateDto);
            return new BaseResponse<>("프로젝트 생성 완료!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ApiOperation(
            value = "프로젝트 상세 조회",
            notes = "프로젝트 ID를 입력받아 해당 프로젝트의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(code = 2230, message = "해당 프로젝트가 존재하지 않습니다.")
    })
    @GetMapping("/project/{id}")
    public BaseResponse<ProjectDetailDto> getProjectDto(@PathVariable Long id) {
        try {
            return new BaseResponse<>(projectService.getProjectDetailDto(id));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "프로젝트 다중 조건 검색",
            notes = "키워드, 지역, 기술 태그를 입력받아 검색된 프로젝트를 반환"
    )
    @PostMapping("/project/search")
    public BaseResponse<List<ProjectDto>> getSearchProjects(@RequestBody ProjectSearchCond cond, @RequestParam("page") int page) {
        return new BaseResponse<>(projectService.getSearchProjects(cond, page));
    }

    @ApiOperation(
            value = "최신 프로젝트 페이지(Home)",
            notes = "Home 페이지로 최신 프로젝트 순으로 10개 반환"
    )
    @GetMapping("/home")
    public BaseResponse<List<ProjectDto>> getRecentProjects() {
        projectService.init();
        List<ProjectDto> projects = projectService.getRecentProjects();
        return new BaseResponse<>(projects);
    }

}
