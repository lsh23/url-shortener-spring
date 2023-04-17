package com.example.urlshortener.domain.member.api;

import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import com.example.urlshortener.domain.member.dto.SignUpReqBuilder;
import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.test.IntegrationTest;
import com.example.urlshortener.test.setup.domain.MemberSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends IntegrationTest {

    @Autowired
    private MemberSetup memberSetup;

    @Test
    public void 회원가입_성공() throws Exception {
        //given
        final Member member = MemberBuilder.build();
        final String email = member.getEmail();
        final String password = member.getPassword();

        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        // when
        ResultActions resultActions = mvc.perform(post("/api/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpReq)))
                .andDo((print()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("email").value(email));
    }

    @Test
    public void 회원가입_실패() throws Exception {
        //given
        final Member member = memberSetup.save();
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        // when
        ResultActions resultActions = mvc.perform(post("/api/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpReq)))
                .andDo((print()));

        // then
        ErrorCode emailDuplication = ErrorCode.EMAIL_DUPLICATION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(emailDuplication.getMessage()))
                .andExpect(jsonPath("code").value(emailDuplication.getCode()));
    }

//    @Test
//    public void 멤버단일조회_성공() throws Exception {
//        // given
//        final Member member = memberSetup.save();
//        Long id = member.getId();
//        // when
//        ResultActions resultActions = mvc.perform(get("/api/member/" + id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo((print()));
//
//        // then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("email").value(member.getEmail()));
//    }
//
//    @Test
//    public void 멤버단일조회_실패() throws Exception {
//        // given
//        final Member member = memberSetup.save();
//        Long id = member.getId() + 1;
//        // when
//        ResultActions resultActions = mvc.perform(get("/api/member/" + id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo((print()));
//
//        // then
//        resultActions.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("code").value(ErrorCode.ENTITY_NOT_FOUND.getCode()));
//    }
}