package com.example.urlshortener.domain.member.api;

import com.example.urlshortener.domain.member.application.MemberService;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import com.example.urlshortener.domain.member.dto.SignUpReqBuilder;
import com.example.urlshortener.test.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest extends ControllerTest {


    @MockBean
    private MemberService memberService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Override
    protected Object initController() {
        return new MemberController(memberService, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입에 대한 요청이 들어오면, 회원가입 처리를 한다.")
    public void signup() throws Exception {
        //given
        final Member member = MemberBuilder.build();
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        MemberResponse expected = MemberResponse.builder().id(1L).email("test@test.com").build();

        given(memberService.registerMember(any(SignUpReq.class))).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/api/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(signUpReq)))
                .andDo(print())
                .andDo(document("member-signup",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING)
                                                .description("email(ID)"),
                                        fieldWithPath("password").type(JsonFieldType.STRING)
                                                .optional()
                                                .description("password")
                                ),
                                responseHeaders(
                                        headerWithName("Location").description("생성된 member id")
                                )
                        )
                );

        // then
        resultActions
                .andExpect(status().isCreated());
    }
}