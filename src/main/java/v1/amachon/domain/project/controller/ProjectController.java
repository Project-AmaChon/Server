package v1.amachon.domain.project.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.domain.project.service.dto.project.*;
import v1.amachon.domain.project.service.dto.recruit.RecruitManagementDto;
import v1.amachon.domain.project.service.ProjectService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"프로젝트 API"})
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
})
public class ProjectController {

    private final ProjectService projectService;

    @ApiOperation(
            value = "프로젝트 생성 페이지: 새 프로젝트 생성",
            notes = "모집인원수, 기간, 제목, 태그 등을 입력받아 해당 정보를 토대로 프로젝트 게시글 생성, 작성자는 리더로 위임"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2210, message = "지역 태그 값이 올바르지 않습니다."),
            @ApiResponse(code = 2220, message = "기술 태그 값이 올바르지 않습니다."),
            @ApiResponse(code = 4001, message = "서버 에러입니다.(S3)"),
    })
    @PostMapping("/project")
    public ResponseEntity<Void> createProject(@RequestHeader("Authorization") String accessToken, @RequestBody ProjectCreateRequestDto projectCreateDto) {
        projectService.createProject(projectCreateDto);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "프로젝트 수정",
            notes = "프로젝트 수정 페이지 정보 받아오기"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2240, message = "해당 프로젝트는 존재하지 않습니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
    })
    @GetMapping("/project/{projectId}/modify")
    public ResponseEntity<ProjectModifyDto> getModifyProject(@RequestHeader("Authorization") String accessToken,
                                                             @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.getModifyProject(projectId));
    }

    @ApiOperation(
            value = "프로젝트 수정",
            notes = "프로젝트 수정하기"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2240, message = "해당 프로젝트는 존재하지 않습니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
            @ApiResponse(code = 2210, message = "지역 태그 값이 올바르지 않습니다."),
            @ApiResponse(code = 2220, message = "기술 태그 값이 올바르지 않습니다."),
    })
    @PatchMapping("/project/{projectId}/modify")
    public ResponseEntity<Void> modifyProject(@RequestHeader("Authorization") String accessToken,
                                              @PathVariable("projectId") Long projectId,
                                              @ModelAttribute ProjectModifyDto projectModifyDto,
                                              @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        projectService.modifyProject(projectId, projectModifyDto, images);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "프로젝트 삭제",
            notes = "프로젝트 삭제하기(상태 값 변경(EXPIRED))"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2240, message = "해당 프로젝트는 존재하지 않습니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
    })
    @PostMapping("/project/{projectId}/delete")
    public ResponseEntity<Void> deleteProject(@RequestHeader("Authorization") String accessToken, @PathVariable("projectId") Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "프로젝트 상세 조회",
            notes = "프로젝트 ID를 입력받아 해당 프로젝트의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2240, message = "해당 프로젝트는 존재하지 않습니다.")
    })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectDetailDto> getProjectDto(@PathVariable("projectId") Long projectId,
                                                          @RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(projectService.getProjectDetailDto(projectId));
    }

    @ApiOperation(
            value = "프로젝트 다중 조건 검색",
            notes = "키워드, 지역, 기술 태그를 입력받아 검색된 프로젝트를 반환"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2000, message = "입력값을 확인해주세요."),
    })
    @PostMapping("/project/search")
    public ResponseEntity<List<ProjectDto>> getSearchProjects(@RequestBody ProjectSearchCond cond,
                                                              @RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(projectService.getSearchProjects(cond));
    }

    @ApiOperation(
            value = "최신 프로젝트 페이지(Home)",
            notes = "Home 페이지로 최신 프로젝트 순으로 10개 반환"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2000, message = "입력값을 확인해주세요."),
    })
    @GetMapping("/home")
    public ResponseEntity<List<ProjectDto>> getRecentProjects(@RequestHeader("Authorization") String accessToken)  {
        return ResponseEntity.ok(projectService.getRecentProjects());
    }

    @ApiOperation(
            value = "프로젝트 참가 신청",
            notes = "프로젝트에 참가 신청을 하고, 신청이 수락되면 팀 멤버로 등록"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2006, message = "리더는 프로젝트에 참가 신청할 수 없습니다."),
            @ApiResponse(code = 2230, message = "이미 참여 중인 프로젝트입니다"),
            @ApiResponse(code = 2240, message = "해당 프로젝트는 존재하지 않습니다."),
            @ApiResponse(code = 2251, message = "이미 지원한 프로젝트 입니다.")

    })
    @PostMapping("/project/{projectId}/apply")
    public ResponseEntity<Void> projectApply(@PathVariable("projectId") Long projectId,
                                             @RequestHeader("Authorization") String accessToken) {
        projectService.projectApply(projectId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "팀원 내보내기",
            notes = "팀원 추방 기능"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
            @ApiResponse(code = 2070, message = "해당 팀원은 존재하지 않습니다.")
    })
    @PostMapping("/project/{projectId}/kick/{teamMemberId}")
    public ResponseEntity<Void> kickTeamMember(@PathVariable("teamMemberId") Long teamMemberId,
                                               @RequestHeader("Authorization") String accessToken) {
        projectService.kickTeamMember(teamMemberId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "프로젝트 참가 신청 현황",
            notes = "프로젝트에 참기 신청을 한 사람들의 간단한 프로필 반환"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
            @ApiResponse(code = 2240, message = "해당 프로젝트가 존재하지 않습니다."),
    })
    @GetMapping("/project/{projectId}/recruit-list")
    public ResponseEntity<List<RecruitManagementDto>> getRecruitList(@PathVariable("projectId") Long projectId,
                                                                     @RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(projectService.getRecruitList(projectId));
    }

    @ApiOperation(
            value = "프로젝트 인원 추천",
            notes = "프로젝트에 적합한 사람들을 추천"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
            @ApiResponse(code = 2240, message = "해당 프로젝트가 존재하지 않습니다.")
    })
    @GetMapping("/project/{projectId}/recommend-teamMember")
    public ResponseEntity<List<RecruitManagementDto>> getRecommendMembers(@PathVariable("projectId") Long projectId,
                                                                          @RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(projectService.getRecommendMember(projectId));
    }

    @ApiOperation(
            value = "참가 신청 수락",
            notes = "프로젝트 참가 신청에 대한 수락 요청"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
            @ApiResponse(code = 2050, message = "신청 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/project/apply/accept/{recruitId}")
    public ResponseEntity<Void> acceptRecruitApply(@PathVariable("recruitId") Long recruitId,
                                                   @RequestHeader("Authorization") String accessToken) {
        projectService.recruitAccept(recruitId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "참가 신청 거절",
            notes = "프로젝트 참가 신청에 대한 거절 요청"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2003, message = "권한이 없습니다."),
            @ApiResponse(code = 2050, message = "신청 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/project/apply/reject/{recruitId}")
    public ResponseEntity<Void> rejectRecruitApply(@PathVariable("recruitId") Long recruitId,
                                                   @RequestHeader("Authorization") String accessToken) {
        projectService.recruitReject(recruitId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "프로젝트 관리",
            notes = "참여중인 프로젝트, 생성한 프로젝트를 조회"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    })
    @GetMapping("/project/management")
    public ResponseEntity<ProjectManagementDto> getProjectManagement(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(projectService.getProjectManagement());
    }
}
