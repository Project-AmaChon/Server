package v1.amachon.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.mail.service.dto.CertificationDto;
import v1.amachon.mail.service.EmailService;
import v1.amachon.member.service.dto.join.EmailDto;
import v1.amachon.member.service.dto.join.NicknameDto;
import v1.amachon.member.service.dto.login.LoginDto;
import v1.amachon.member.service.dto.login.TokenDto;
import v1.amachon.member.service.AuthService;
import v1.amachon.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@Api(tags = {"인증 API"})
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final MemberService memberService;

    @ApiOperation(value = "이메일 중복 체크, 이메일 인증 코드 발송", notes = "이메일 중복 시에 DUPLICATED_EMAIL(3011)에러를 리턴하고 중복된 이메일이 아니라면 인증 코드 발송")
    @PostMapping("/join/send-verification-code")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody EmailDto emailDto) {
        memberService.isDuplicateEmail(emailDto.getEmail());
        emailService.sendVerificationCode(emailDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "인증 코드 확인", notes = "인증 코드가 올바르지 않으면 INVALID_CODE(2011) 에러를 리턴")
    @PostMapping("/join/certification")
    public ResponseEntity<Void> certification(@RequestBody CertificationDto certificationDto) {
        authService.certify(certificationDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "닉네임 중복 확인", notes = "닉네임이 중복되면 DUPLICATE_NICKNAME(3012) 에러를 리턴")
    @PostMapping("/join/check-nickname")
    public ResponseEntity<Void> checkNickname(@RequestBody NicknameDto nicknameDto) {
        memberService.isDuplicateNickname(nicknameDto.getNickname());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "로그인", notes = "아이디(이메일)과 패스워드가 일치하면 정상적으로 로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }
}
