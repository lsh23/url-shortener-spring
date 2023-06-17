package com.example.urlshortener.domain.auth.api;

import com.example.urlshortener.domain.auth.application.AuthService;
import com.example.urlshortener.domain.auth.dto.*;
import com.example.urlshortener.test.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest extends ControllerTest {

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("로그인 요청이 들어오면 로그인 요청을 처리한다.")
    void signin() throws Exception {
        // given
        SignInReq request = SignInReq.builder()
                .email("solver@test.com")
                .password("password")
                .build();

        BasicLoginResponse expected = BasicLoginResponse.builder()
                .email("solver@test.com")
                .accessToken("TOKEN")
                .refreshToken("REFRESH_TOKEN")
                .build();

        given(authService.signIn(any())).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/signin")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("auth-signin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("email(ID)"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("password")
                        ),
                        responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("email(ID)"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("access token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("refresh token")
                        )
                ));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("OAUTH 로그인 요청이 들어오면 로그인 요청을 처리한다.")
    void oauthSignin() throws Exception {
        // given

        OAuthLoginResponse expected = OAuthLoginResponse.builder()
                .email("solver@test.com")
                .accessToken("TOKEN")
                .refreshToken("REFRESH_TOKEN")
                .build();

        given(authService.oauthSignIn(any(), any())).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/auth/oauth")
                        .characterEncoding("utf-8")
                        .param("provider", "PROVIDER")
                        .param("code", "CODE"))
                .andDo(print())
                .andDo(document("auth-oauth",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("provider").description("OAuth Provider e.g. github, ... "),
                                parameterWithName("code").description("Authorization code from OAuth Provider")),
                        responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("email(ID)"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("access token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("refresh token")
                        )
                ));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Refresh Token과 함께 인증 refresh 요청이 들어오면 처리한다.")
    void refresh() throws Exception {
        // given
        RefreshAuthRequest request = RefreshAuthRequest.builder()
                .email("solver@test.com")
                .refreshToken("REFRESH_TOKEN")
                .build();
        RefreshAuthResponse expected = RefreshAuthResponse.builder()
                .accessToken("ACCESS_TOKEN")
                .build();

        given(authService.refreshAuth(any())).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/auth/refresh")
                        .characterEncoding("utf-8")
                        .header("authorization", "Bearer TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("auth-refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("email(ID)"),
                                fieldWithPath("refresh_token").type(JsonFieldType.STRING)
                                        .description("refresh token")
                        ),
                        responseFields(
                                fieldWithPath("access_token").type(JsonFieldType.STRING)
                                        .description("access token")
                        )
                ));

        // then
        resultActions.andExpect(status().isOk());
    }
}