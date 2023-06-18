package com.example.urlshortener.domain.auth.api;

import com.example.urlshortener.domain.auth.application.OauthHandler;
import com.example.urlshortener.domain.auth.dao.RefreshTokenRepository;
import com.example.urlshortener.domain.auth.dto.*;
import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import com.example.urlshortener.domain.member.dto.SignUpReqBuilder;
import com.example.urlshortener.test.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        refreshTokenRepository.deleteAll();
        memberRepository.deleteAllInBatch();
    }

    @MockBean
    private OauthHandler oauthHandler;

    @Test
    @DisplayName("ID/PW 로그인 API")
    void signIn() {
        // given
        requestSignup();

        SignInReq request = SignInReq.builder()
                .email("solver@test.com")
                .password("password")
                .build();


        // when
        ExtractableResponse<Response> extract =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .when()
                        .post("/api/auth/signin")
                        .then().log().all()
                        .extract();


        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("OAUTH 로그인 API")
    void oauthSignIn() {
        // given
        Member member = requestSignup();

        OauthUserInformation oauthUserInformation = OauthUserInformation.builder().email(member.getEmail()).build();

        final String provider = "github";
        final String code = "code";

        BDDMockito.given(oauthHandler.getUserInfoFromAuthCode(provider, code)).willReturn(oauthUserInformation);

        // when
        ExtractableResponse<Response> extract =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/api/auth/oauth?" + "provider=" + provider + "&code=" + code)
                        .then().log().all()
                        .extract();

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Refresh Auth API")
    void refreshAuth() {
        // given
        requestSignup();
        BasicLoginResponse basicLoginResponse = requestSignin();

        RefreshAuthRequest request = RefreshAuthRequest.builder()
                .email(basicLoginResponse.getEmail())
                .refreshToken(basicLoginResponse.getRefreshToken())
                .build();

        // when
        ExtractableResponse<Response> extract =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", "Bearer " + basicLoginResponse.getAccessToken())
                        .body(request)
                        .when()
                        .get("/api/auth/refresh")
                        .then().log().all()
                        .extract();

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    private Member requestSignup() {
        final Member member = MemberBuilder.build();
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        ExtractableResponse<Response> extract =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(signUpReq)
                        .when()
                        .post("/api/members/signup")
                        .then().log().all()
                        .extract();

        return member;
    }

    private BasicLoginResponse requestSignin() {
        final Member member = MemberBuilder.build();
        final String email = member.getEmail();
        final String password = member.getPassword();
        SignInReq request = SignInReq.builder()
                .email(email)
                .password(password)
                .build();

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/auth/signin")
                .then().log().all()
                .extract()
                .response()
                .as(BasicLoginResponse.class);
    }
}
