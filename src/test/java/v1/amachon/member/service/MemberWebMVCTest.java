package v1.amachon.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import v1.amachon.common.WebMVCTest;
import v1.amachon.fixtures.MemberFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MemberWebMVCTest extends WebMVCTest {

    @MockBean
    MemberService memberService;

    @Test
    void getMyProfile() throws Exception {
        memberRepository.save(MemberFixtures.정우());
        String token = "Bearer " + jwtTokenUtil.generateAccessToken(MemberFixtures.정우_이메일);

        mockMvc.perform(get("/my-page")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isAccepted())
                .andDo(print());
    }
}

