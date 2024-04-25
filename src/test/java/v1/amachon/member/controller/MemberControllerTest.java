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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {

    @DisplayName("올바르지 않은 토큰을 사용 시 401 에러를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"invalid token", "Bearer 1234", "Bearer "})
    void invalidToken(String token) throws Exception {
        // when, then
        mockMvc.perform(get("/my-page")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원 가입에 성공한다")
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


    @DisplayName("이메일 형식이 올바르지 않으면 회원 가입에 실패한다")
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

    @DisplayName("로그인되지 않은 사용자는 마이페이지를 조회할 수 없다")
    @Test
    void failGetMyPageByNotLoggedUser() throws Exception {
        // when, then
        mockMvc.perform(get("/my-page"))
                .andDo(print())
                .andExpect(status().isBadRequest());
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
