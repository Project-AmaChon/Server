package v1.amachon.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import v1.amachon.common.ControllerTest;
import v1.amachon.member.service.dto.join.JoinRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static v1.amachon.fixtures.MemberFixtures.종범_이메일;

public class MemberControllerTest extends ControllerTest {

    @DisplayName("올바르지 않은 형식의 토큰 사용 시 401 에러를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"invalid token", "Bearer 1234", "Bearer "})
    void invalidFormatToken(String token) throws Exception {
        // when, then
        mockMvc.perform(get("/my-page")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("토큰 형식이 올바르지 않습니다."));
    }

    @DisplayName("토큰 payload의 username과 로그인 되어있는 유저의 username이 동일하지 않은 토큰 사용 시 401 에러를 반환한다")
    @Test
    void notEqualsTokenAndUserDetail() throws Exception {
        // given
        String token = jwtTokenUtil.generateAccessToken(종범_이메일);

        // when, then
        mockMvc.perform(get("/my-page")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + token))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("토큰과 username이 일치하지 않습니다."));
    }

    @DisplayName("가입 정보가 모두 올바르면 회원 가입에 성공한다")
    @Test
    void joinSuccess() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
                .email("acg6138@naver.com")
                .nickname("이정우")
                .password("mypassword").build();

        // when, then
        mockMvc.perform(post("/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("이메일 형식이 올바르지 않으면 회원 가입에 실패하고 400 에러를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"acg6138#naver.com", "acg6138@@navercom", "invaildEmail"})
    void joinFail(String email) throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
                .email(email)
                .nickname("이정우")
                .password("mypassword").build();

        // when, then
        mockMvc.perform(post("/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인되지 않은 사용자는 마이페이지를 조회하는 경우 401 에러를 반환한다")
    @Test
    void failGetMyPageByNotLoggedUser() throws Exception {
        // when, then
        mockMvc.perform(get("/my-page"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
    }

    @DisplayName("프로필 조회 시 정상적이지 않은 프로필 ID인 경우 400 에러를 반환한다")
    @Test
    void notFoundUserProfile() throws Exception {
        // when, then
        mockMvc.perform(get("/profile/user"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
