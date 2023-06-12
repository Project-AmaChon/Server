package v1.amachon.domain.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.member.dto.ProfileDto;
import v1.amachon.domain.member.dto.join.JoinDto;
import v1.amachon.domain.member.dto.login.TokenDto;
import v1.amachon.domain.member.service.MemberService;

import java.io.IOException;

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
            @ApiResponse(code = 2230, message = "로그인이 필요합니다.")
    })
    @GetMapping("/my-page")
    public BaseResponse<ProfileDto> getMyProfile(@RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(memberService.getMyProfile());
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "타유저의 프로필 정보 조회",
            notes = "타유저의 프로필 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(code = 2230, message = "로그인이 필요합니다.")
    })
    @GetMapping("/profile/{id}")
    public BaseResponse<ProfileDto> getProfile(@PathVariable("id") Long id) {
        try {
            return new BaseResponse<>(memberService.getProfile(id));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @PatchMapping("/my-page/profile-image")
    public BaseResponse<String> changeProfileImage(@RequestHeader("Authorization")String accessToken, @RequestPart(value = "image", required = false) MultipartFile profile) throws IOException {
        try {
            memberService.changeProfileImage(profile);
            return new BaseResponse<>("프로필 이미지가 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(value = "프로필 정보 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @GetMapping("/my-page/profile")
    public BaseResponse<ProfileDto> getProfile(@RequestHeader("Authorization")String accessToken, @RequestBody ProfileDto profileDto) throws IOException {
        try {
            ProfileDto profile = memberService.getMyProfile();
            return new BaseResponse<>(profile);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(value = "프로필 정보 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @PatchMapping("/my-page/profile")
    public BaseResponse<String> changeProfile(@RequestHeader("Authorization")String accessToken, @RequestBody ProfileDto profileDto) throws IOException {
        try {
            memberService.changeProfile(profileDto);
            return new BaseResponse<>("프로필 정보가 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
