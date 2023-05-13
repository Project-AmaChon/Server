package v1.amachon.domain.member.controller;

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
import v1.amachon.domain.mail.dto.CertificationDto;
import v1.amachon.domain.mail.service.EmailService;
import v1.amachon.domain.member.dto.join.EmailDto;
import v1.amachon.domain.member.dto.join.NicknameDto;
import v1.amachon.domain.member.dto.login.LoginDto;
import v1.amachon.domain.member.dto.login.TokenDto;
import v1.amachon.domain.member.service.AuthService;
import v1.amachon.domain.member.service.MemberService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
@Api(tags = {"인증 API"})

public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final MemberService memberService;

    @ApiOperation(
            value = "이메일 중복 체크, 이메일 인증 코드 발송",
            notes = "이메일 중복 시에 DUPLICATED_EMAIL(3011)에러를 리턴하고 중복된 이메일이 아니라면 인증 코드 발송"
    )
    @ApiResponse(code = 3011, message = "중복된 이메일 입니다.")
    @PostMapping("/join/send-verification-code")
    public BaseResponse<String> sendVerificationCode(@RequestBody EmailDto emailDto) throws MessagingException, UnsupportedEncodingException {
        try{
            memberService.isDuplicateEmail(emailDto.getEmail());
            emailService.sendVerificationCode(emailDto.getEmail());
            return new BaseResponse<>("이메일 인증 코드 발송완료!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "인증 코드 확인",
            notes = "인증 코드가 올바르지 않으면 INVALID_CODE(2011) 에러를 리턴"
    )
    @ApiResponses({
            @ApiResponse(code = 2011, message = "인증 코드가 올바르지 않습니다."),
            @ApiResponse(code = 2100, message = "만료된 인증 코드입니다."),
    })
    @PostMapping("/join/certification")
    public BaseResponse<String> certification(@RequestBody CertificationDto certificationDto) {
        try {
            authService.certify(certificationDto);
            return new BaseResponse<>("이메일 인증 완료!");
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "닉네임 중복 확인",
            notes = "닉네임이 중복되면 DUPLICATE_NICKNAME(3012) 에러를 리턴"
    )
    @ApiResponse(code = 3012, message = "이미 존재하는 닉네임입니다.")
    @PostMapping("/join/check-nickname")
    public BaseResponse<String> checkNickname(@RequestBody NicknameDto nicknameDto) {
        try {
            memberService.isDuplicateNickname(nicknameDto);
            return new BaseResponse<>("닉네임 중복 확인 완료!");
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "로그인",
            notes = "아이디(이메일)과 패스워드가 일치하면 정상적으로 로그인"
    )
    @ApiResponses({
            @ApiResponse(code = 3014, message = "없는 아이디입니다."),
            @ApiResponse(code = 3015, message = "비밀번호가 다릅니다."),
            @ApiResponse(code = 3016, message = "탈퇴한 회원입니다.")
    })
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@RequestBody LoginDto loginDto) {
        try{
            TokenDto tokenDto = authService.login(loginDto);
            return new BaseResponse<>(tokenDto);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
