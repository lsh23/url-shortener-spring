package com.example.urlshortener.domain.member.api;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Java6Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        memberRepository.deleteAllInBatch();
    }
    @Test
    @DisplayName("회원가입 요청 API")
    public void signup() throws Exception {

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

        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}