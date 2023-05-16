package v1.amachon.domain.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.member.dto.ProfileDto;
import v1.amachon.domain.member.dto.join.JoinDto;
import v1.amachon.domain.member.dto.login.TokenDto;
import v1.amachon.domain.member.entity.Profile;
import v1.amachon.domain.member.service.MemberService;
import v1.amachon.domain.project.dto.ProjectDetailDto;

@RestController
@RequiredArgsConstructor
@Api(tags = "회원 정보 API")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(
            value = "회원 가입 및 자동 로그인",
            notes = "회원 가입이 성공적으로 진행될 시 자동으로 로그인"
    )
    @PostMapping("/join")
    public BaseResponse<TokenDto> join(@RequestBody JoinDto joinDto) throws BaseException {
        TokenDto tokenDto = memberService.join(joinDto);
        return new BaseResponse<>(tokenDto);
    }

    @ApiOperation(
            value = "마이페이지 조회",
            notes = "로그인 되어있는 유저의 마이페이지를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(code = 2230, message = "해당 프로젝트가 존재하지 않습니다.")
    })
    @GetMapping("/my-page")
    public BaseResponse<ProfileDto> getProfile(@RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(memberService.getProfile());
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
