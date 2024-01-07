package v1.amachon.domain.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import v1.amachon.domain.member.service.dto.ProfileResponseDto;
import v1.amachon.domain.member.service.dto.join.JoinDto;
import v1.amachon.domain.member.service.dto.login.TokenDto;
import v1.amachon.domain.member.service.MemberService;

import javax.validation.Valid;
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
    public ResponseEntity<TokenDto> join(@RequestBody @Valid JoinDto joinDto) {
        return ResponseEntity.ok(memberService.join(joinDto));
    }

    @ApiOperation(
            value = "마이페이지 조회",
            notes = "로그인 되어있는 유저의 마이페이지를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(code = 2230, message = "로그인이 필요합니다.")
    })
    @GetMapping("/my-page")
    public ResponseEntity<ProfileResponseDto> getMyProfile(@RequestHeader("Authorization")String accessToken) {
            return ResponseEntity.ok(memberService.getMyProfile());
    }

    @ApiOperation(
            value = "타유저의 프로필 정보 조회",
            notes = "타유저의 프로필 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(code = 2230, message = "로그인이 필요합니다.")
    })
    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable("id") Long id) {
            return ResponseEntity.ok(memberService.getProfile(id));
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @PatchMapping("/my-page/profile-image")
    public ResponseEntity<Void> changeProfileImage(@RequestHeader("Authorization")String accessToken,
                                                     @RequestPart(value = "image", required = false) MultipartFile profile) {
        memberService.changeProfileImage(profile);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "프로필 정보 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @GetMapping("/my-page/profile")
    public ResponseEntity<ProfileResponseDto> getProfile(@RequestHeader("Authorization")String accessToken, @RequestBody ProfileResponseDto profileResponseDto) throws IOException {
            ProfileResponseDto profile = memberService.getMyProfile();
            return ResponseEntity.ok(profile);
    }

    @ApiOperation(value = "프로필 정보 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @PatchMapping("/my-page/profile")
    public ResponseEntity<Void> changeProfile(@RequestHeader("Authorization")String accessToken, @RequestBody ProfileResponseDto profileResponseDto) throws IOException {
            memberService.changeProfile(profileResponseDto);
        return ResponseEntity.noContent().build();
    }

}
