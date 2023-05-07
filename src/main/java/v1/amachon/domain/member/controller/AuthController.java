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
    @ApiResponses({
            @ApiResponse(code = 3011, message = "중복된 이메일 입니다.")
    })
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
}
