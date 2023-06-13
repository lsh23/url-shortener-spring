package com.example.urlshortener.domain.url.api;

import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UrlAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        urlRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("URL Shorten Post API")
    void shortenUrl(){
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
    @DisplayName("URL Shorten Url redirect API")
    void redirect() throws Exception {
        // given
        ShortenUrlRequest request = ShortenUrlRequest.builder()
                .fullUrl("http://localhost:"+ RestAssured.port+"/actuator/health")
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
                        .get("/"+hash)
                        .then().log().all()
                        .extract();

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
