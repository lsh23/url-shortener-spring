package com.example.urlshortener.domain.url.api;

import com.example.urlshortener.domain.auth.dao.RefreshTokenRepository;
import com.example.urlshortener.domain.auth.dto.BasicLoginResponse;
import com.example.urlshortener.domain.auth.dto.SignInReq;
import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import com.example.urlshortener.domain.member.dto.SignUpReqBuilder;
import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.dto.*;
import com.example.urlshortener.test.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UrlAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        urlRepository.deleteAllInBatch();
        refreshTokenRepository.deleteAll();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("URL Shorten Post API - 비로그인 상태")
    void shortenUrl() {
        // given
        ShortenUrlRequest request = ShortenUrlRequest.builder()
                .fullUrl("www.test.com")
                .build();

        // when
        ExtractableResponse<Response> extract =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .when()
                        .post("/api/url")
                        .then().log().all()
                        .extract();

        ShortenUrlResponse body = extract.body().as(ShortenUrlResponse.class);

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(body.getFullUrl()).isEqualTo("www.test.com");

    }

    @Test
    @DisplayName("URL Shorten Post API - 로그인 상태")
    void shortenUrlForMe() {

        // given
        requestSignup();
        BasicLoginResponse basicLoginResponse = requestSignin();
        Long memberId = basicLoginResponse.getId();

        ShortenUrlForMeRequest request = ShortenUrlForMeRequest.builder()
                .memberId(memberId)
                .fullUrl("www.test.com")
                .build();

        // when
        ExtractableResponse<Response> extract =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", "Bearer " + basicLoginResponse.getAccessToken())
                        .body(request)
                        .when()
                        .post("/api/me/url")
                        .then().log().all()
                        .extract();

        ShortenUrlResponse body = extract.body().as(ShortenUrlResponse.class);

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(body.getFullUrl()).isEqualTo("www.test.com");

    }

    @Test
    @DisplayName("URL Shorten Url redirect API")
    void redirect() throws Exception {
        // given
        ShortenUrlRequest request = ShortenUrlRequest.builder()
                .fullUrl("http://localhost:" + RestAssured.port + "/actuator/health")
                .build();

        ExtractableResponse<Response> expected = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/url")
                .then().log().all()
                .extract();

        ShortenUrlResponse body = expected.body().as(ShortenUrlResponse.class);
        String hash = body.getHash();

        // when
        ExtractableResponse<Response> extract =
                given().log().all()
                        .when()
                        .pathParam("hash", hash)
                        .get("/{hash}")
                        .then().log().all()
                        .extract();

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("URL Shorten Url GET API - LIST")
    void getList() throws Exception {
        // given
        requestSignup();
        BasicLoginResponse basicLoginResponse = requestSignin();
        Long memberId = basicLoginResponse.getId();

        ShortenUrlForMeRequest request = ShortenUrlForMeRequest.builder()
                .memberId(memberId)
                .fullUrl("www.test.com")
                .build();


        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", "Bearer " + basicLoginResponse.getAccessToken())
                .body(request)
                .when()
                .post("/api/me/url")
                .then().log().all();

        // when
        ExtractableResponse<Response> extract = given().log().all()
                .when()
                .queryParam("memberId", memberId)
                .get("/api/url")
                .then().log().all()
                .extract();

        ShortenUrlsResponse body = extract.body().as(ShortenUrlsResponse.class);
        List<ShortenUrlResponse> urls = body.getUrls();

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(urls.size()).isNotZero();
    }

    @Test
    @DisplayName("URL Shorten Url PUT API")
    void update() throws Exception {
        // given
        requestSignup();
        BasicLoginResponse basicLoginResponse = requestSignin();
        Long memberId = basicLoginResponse.getId();
        String accessToken = basicLoginResponse.getAccessToken();
        ShortenUrlResponse shortenUrlResponse = saveUrl(memberId, accessToken);

        ShortenUrlUpdateRequest request = ShortenUrlUpdateRequest.builder()
                .memberId(memberId)
                .expireAt(LocalDateTime.now().plusDays(14))
                .build();

        // when
        ExtractableResponse<Response> extract = given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", "Bearer " + basicLoginResponse.getAccessToken())
                .body(request)
                .pathParam("hash", shortenUrlResponse.getHash())
                .put("/api/me/url/{hash}")
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

    private ShortenUrlResponse saveUrl(Long memberId, String accessToken) {
        ShortenUrlForMeRequest request = ShortenUrlForMeRequest.builder()
                .memberId(memberId)
                .fullUrl("www.test.com")
                .build();

        // when
        ExtractableResponse<Response> extract =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", "Bearer " + accessToken)
                        .body(request)
                        .when()
                        .post("/api/me/url")
                        .then().log().all()
                        .extract();

        return extract.body().as(ShortenUrlResponse.class);
    }
}
