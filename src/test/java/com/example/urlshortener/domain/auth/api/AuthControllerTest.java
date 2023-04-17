package com.example.urlshortener.domain.auth.api;

import com.example.urlshortener.domain.auth.dto.SignInReq;
import com.example.urlshortener.domain.auth.dto.SignInReqBuilder;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.test.IntegrationTest;
import com.example.urlshortener.test.setup.domain.MemberSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.urlshortener.global.error.ErrorCode.EMAIL_INVALID;
import static com.example.urlshortener.global.error.ErrorCode.PASSWORD_INVALID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends IntegrationTest {
    @Autowired
    private MemberSetup memberSetup;

    @Test
    void 로그인_성공() throws Exception {
        // given
        Member member = memberSetup.save();
        String email = member.getEmail();
        String password = memberSetup.getRawPassword();
        SignInReq signInReq = SignInReqBuilder.build(email, password);

        // when
        ResultActions resultActions = mvc.perform(post("/api/member/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInReq)))
                .andDo((print()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 로그인_실패_없는_email() throws Exception {
        // given
        String email = "WRONG@WRONG.com";
        String password = "WRONG";
        SignInReq signInReq = SignInReqBuilder.build(email, password);

        // when
        ResultActions resultActions = mvc.perform(post("/api/member/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInReq)))
                .andDo((print()));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(EMAIL_INVALID.getMessage()))
                .andExpect(jsonPath("code").value(EMAIL_INVALID.getCode()));
    }

    @Test
    void 로그인_pw_불일치() throws Exception{
        // given
        Member member = memberSetup.save();
        String email = member.getEmail();
        String password = "WRONG";
        SignInReq signInReq = SignInReqBuilder.build(email, password);

        // when
        ResultActions resultActions = mvc.perform(post("/api/member/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInReq)))
                .andDo((print()));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(PASSWORD_INVALID.getMessage()))
                .andExpect(jsonPath("code").value(PASSWORD_INVALID.getCode()));
    }
}