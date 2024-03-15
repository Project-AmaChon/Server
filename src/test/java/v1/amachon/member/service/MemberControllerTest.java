package v1.amachon.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import v1.amachon.common.WebMVCTest;
import v1.amachon.common.WithCustomMockUser;
import v1.amachon.fixtures.MemberFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberWebMVCTest extends WebMVCTest {

    @Test
    @WithCustomMockUser
    void getMyProfile() throws Exception {
        memberRepository.save(MemberFixtures.정우());
        String token = "Bearer " + jwtTokenUtil.generateAccessToken(MemberFixtures.정우_이메일);
        mockMvc.perform(get("/my-page"))
                .andExpect(status().isOk())
                .andDo(print());

    }
}

