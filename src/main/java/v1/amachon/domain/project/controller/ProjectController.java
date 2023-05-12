package v1.amachon.domain.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.service.ProjectService;

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
  @PostMapping("/project/create")
  public BaseResponse<String> createProject(@RequestBody ProjectCreateRequestDto projectCreateDto) {
    try {
      projectService.createProject(projectCreateDto);
      return new BaseResponse<>("프로젝트 생성 완료!");
    } catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }
}