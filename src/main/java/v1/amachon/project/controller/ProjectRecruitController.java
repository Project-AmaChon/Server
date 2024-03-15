package v1.amachon.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.common.config.security.AuthMemberEmail;
import v1.amachon.project.service.ProjectRecruitService;
import v1.amachon.project.service.response.RecommendMemberResponse;
import v1.amachon.project.service.response.RecruitManagementResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"프로젝트 모집 API"})
public class ProjectRecruitController {

    private final ProjectRecruitService projectRecruitService;

    @ApiOperation(value = "프로젝트 참가 신청", notes = "프로젝트에 참가 신청을 하고, 신청이 수락되면 팀 멤버로 등록")
    @PostMapping("/project/{projectId}/apply")
    public ResponseEntity<Void> projectApply(@Parameter(hidden = true) @AuthMemberEmail String email, @PathVariable("projectId") Long projectId) {
        projectRecruitService.applyForProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "참가 신청 수락", notes = "프로젝트 참가 신청에 대한 수락 요청")
    @GetMapping("/project/apply/accept/{recruitId}")
    public ResponseEntity<Void> acceptRecruitApply(@Parameter(hidden = true) @AuthMemberEmail String email, @PathVariable("recruitId") Long recruitId) {
        projectRecruitService.recruitAccept(recruitId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "참가 신청 거절", notes = "프로젝트 참가 신청에 대한 거절 요청")
    @GetMapping("/project/apply/reject/{recruitId}")
    public ResponseEntity<Void> rejectRecruitApply(@Parameter(hidden = true) @AuthMemberEmail String email, @PathVariable("recruitId") Long recruitId) {
        projectRecruitService.recruitReject(recruitId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "팀원 내보내기", notes = "팀원 추방 기능")
    @PostMapping("/project/{projectId}/kick/{teamMemberId}")
    public ResponseEntity<Void> kickTeamMember(@Parameter(hidden = true) @AuthMemberEmail String email, @PathVariable("teamMemberId") Long teamMemberId) {
        projectRecruitService.kickTeamMember(teamMemberId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "프로젝트 참가 신청 현황", notes = "프로젝트에 참기 신청을 한 사람들의 간단한 프로필 반환")
    @GetMapping("/project/{projectId}/recruit-list")
    public ResponseEntity<List<RecruitManagementResponse>> getRecruitList(@Parameter(hidden = true) @AuthMemberEmail String email, @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectRecruitService.getRecruitList(projectId));
    }

    @ApiOperation(value = "프로젝트 인원 추천", notes = "프로젝트에 적합한 사람들을 추천")
    @GetMapping("/project/{projectId}/recommend-teamMember")
    public ResponseEntity<List<RecommendMemberResponse>> getRecommendMembers(@Parameter(hidden = true) @AuthMemberEmail String email, @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectRecruitService.getRecommendMember(projectId));
    }

}
