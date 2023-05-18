package v1.amachon.domain.project.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.project.dto.ProjectCreateRequestDto;
import v1.amachon.domain.project.dto.ProjectDetailDto;
import v1.amachon.domain.project.dto.ProjectDto;
import v1.amachon.domain.project.dto.ProjectSearchCond;
import v1.amachon.domain.project.dto.recruit.RecruitManagementDto;
import v1.amachon.domain.project.service.ProjectService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"프로젝트 API"})
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
})
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
})
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
    public BaseResponse<String> createProject(@RequestBody ProjectCreateRequestDto projectCreateDto,
                                              @RequestHeader("Authorization")String accessToken) {
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
    @GetMapping("/project/{projectId}")
    public BaseResponse<ProjectDetailDto> getProjectDto(@PathVariable("projectId") Long projectId,
                                                        @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(projectService.getProjectDetailDto(projectId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "프로젝트 다중 조건 검색",
            notes = "키워드, 지역, 기술 태그를 입력받아 검색된 프로젝트를 반환"
    )
    @ApiResponse(code = 2040, message = "태그 정보가 올바르지 않습니다")
    @PostMapping("/project/search")
    public BaseResponse<List<ProjectDto>> getSearchProjects(@RequestBody ProjectSearchCond cond,
                                                            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(projectService.getSearchProjects(cond));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ApiOperation(
            value = "최신 프로젝트 페이지(Home)",
            notes = "Home 페이지로 최신 프로젝트 순으로 10개 반환"
    )
    @GetMapping("/home")
    public BaseResponse<List<ProjectDto>> getRecentProjects(@RequestHeader("Authorization")String accessToken) throws BaseException {
        List<ProjectDto> projects = projectService.getRecentProjects();
        return new BaseResponse<>(projects);
    }

    @ApiOperation(
            value = "프로젝트 참가 신청",
            notes = "프로젝트에 참가 신청을 하고, 신청이 수락되면 팀 멤버로 등록"
    )
    @ApiResponses({
            @ApiResponse(code = 2006, message = "잘못된 접근입니다."),
            @ApiResponse(code = 2230, message = "이미 참여 중인 프로젝트입니다"),

    })
    @PostMapping("/project/{projectId}/apply")
    public BaseResponse<String> projectApply(@PathVariable("projectId") Long projectId,
                                             @RequestHeader("Authorization")String accessToken) {
        try {
            projectService.projectApply(projectId);
            return new BaseResponse<>("참가 신청 완료!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "프로젝트 참가 신청 현황",
            notes = "프로젝트에 참기 신청을 한 사람들의 간단한 프로필 반환"
    )
    @ApiResponses({
            @ApiResponse(code = 2240, message = "해당 프로젝트가 존재하지 않습니다."),
            @ApiResponse(code = 2003, message = "권한이 없는 유저입니다.")
    })
    @GetMapping("/project/{projectId}/recruit-list")
    public BaseResponse<List<RecruitManagementDto>> getRecruitList(@PathVariable("projectId") Long projectId,
                                                                   @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(projectService.getRecruitList(projectId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "프로젝트 인원 추천",
            notes = "프로젝트에 적합한 사람들을 추천"
    )
    @ApiResponses({
            @ApiResponse(code = 2240, message = "해당 프로젝트가 존재하지 않습니다."),
            @ApiResponse(code = 2003, message = "권한이 없는 유저입니다.")

    })
    @GetMapping("/project/{projectId}/recommend-teamMember")
    public BaseResponse<List<RecruitManagementDto>> getRecommendMembers(@PathVariable("projectId") Long projectId,
                                                                        @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(projectService.getRecommendMember(projectId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "참가 신청 수락",
            notes = "프로젝트 참가 신청에 대한 수락 요청"
    )
    @ApiResponses({
            @ApiResponse(code = 2240, message = "해당 프로젝트가 존재하지 않습니다."),
            @ApiResponse(code = 2003, message = "권한이 없는 유저입니다."),
            @ApiResponse(code = 2050, message = "신청 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/project/apply/accept/{recruitId}")
    public BaseResponse<String> acceptRecruitApply(@PathVariable("recruitId") Long recruitId,
                                                   @RequestHeader("Authorization")String accessToken) {
        try {
            projectService.recruitAccept(recruitId);
            return new BaseResponse<>("참가 신청 수락 완료!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "참가 신청 거절",
            notes = "프로젝트 참가 신청에 대한 거절 요청"
    )
    @ApiResponses({
            @ApiResponse(code = 2240, message = "해당 프로젝트가 존재하지 않습니다."),
            @ApiResponse(code = 2003, message = "권한이 없는 유저입니다."),
            @ApiResponse(code = 2050, message = "신청 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/project/apply/reject/{recruitId}")
    public BaseResponse<String> rejectRecruitApply(@PathVariable("recruitId") Long recruitId,
                                                   @RequestHeader("Authorization")String accessToken) {
        try {
            projectService.recruitReject(recruitId);
            return new BaseResponse<>("참가 신청 거절 완료!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
