package v1.amachon.domain.member.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.member.dto.join.JoinDto;
import v1.amachon.domain.member.dto.login.TokenDto;
import v1.amachon.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
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
}
