package v1.amachon.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.member.dto.join.JoinDto;
import v1.amachon.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public BaseResponse<String> join(@RequestBody JoinDto joinDto) {
        memberService.join(joinDto);
        return new BaseResponse<>("회원 가입 성공!");
    }
}
