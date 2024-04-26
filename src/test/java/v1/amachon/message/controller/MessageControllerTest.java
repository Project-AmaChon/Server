package v1.amachon.message.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import v1.amachon.common.ControllerTest;
import v1.amachon.message.service.dto.SendMessageRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static v1.amachon.fixtures.MemberFixtures.정우_이메일;

public class MessageControllerTest extends ControllerTest {

    @DisplayName("보내려는 유저의 ID를 통해 메시지를 보낼 수 있다")
    @Test
    void sendMessageByMemberId() throws Exception {
        // given
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        String token = jwtTokenUtil.generateAccessToken(정우_이메일);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/messages/1/send")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("로그인 되어있지 않은 유저가 메시지를 보내는 경우 401 에러를 반환한다")
    @Test
    void failSendMessageByUnAuthorizedMember() throws Exception {
        // given
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/messages/invalidMemberId/send")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
    }

    @DisplayName("보내려는 유저의 ID가 올바르지 않으면 400 에러를 반환한다")
    @Test
    void failSendMessageByInvalidMemberId() throws Exception {
        // given
        SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
        String token = jwtTokenUtil.generateAccessToken(정우_이메일);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/messages/invalidMemberId/send")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Nested
    @DisplayName("메시지 방의 ID가 올바르지 않으면 400 에러를 반환한다")
    class invalidMessageRoomId {
        @Test
        @DisplayName("메시지 전송")
        void failSendMessage() throws Exception {
            // given
            SendMessageRequest request = SendMessageRequest.builder().content("안녕하세요").build();
            String token = jwtTokenUtil.generateAccessToken(정우_이메일);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/messages/room/invalidMessageRoomId/send")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("메시지 방 조회")
        void failGetMessageRoom() throws Exception {
            // given
            String token = jwtTokenUtil.generateAccessToken(정우_이메일);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.get("/messages/room/invalidMessageRoomId")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("메시지 방 나가기")
        void failLeaveMessageRoom() throws Exception {
            // given
            String token = jwtTokenUtil.generateAccessToken(정우_이메일);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/messages/room/invalidMessageRoomId/leave")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("메시지 ID가 올바르지 않으면 400 에러를 반환한다")
    class invalidMessageId {
        @Test
        @DisplayName("메시지 상세 조회")
        void failGetMessage() throws Exception {
            // given
            String token = jwtTokenUtil.generateAccessToken(정우_이메일);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.get("/messages/invalidMessageId")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("메시지 삭제")
        void failDeleteMessage() throws Exception {
            // given
            String token = jwtTokenUtil.generateAccessToken(정우_이메일);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.delete("/messages/invalidMessageId")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

}
