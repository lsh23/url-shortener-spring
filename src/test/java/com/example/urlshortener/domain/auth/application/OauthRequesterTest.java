package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dto.OauthAccessTokenResponse;
import com.example.urlshortener.domain.auth.dto.OauthUserInfoResponse;
import com.example.urlshortener.test.MockWebServerTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;


class OauthRequesterTest extends MockWebServerTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private WebClient webClient = WebClient.create();
    private OauthRequester oauthRequester;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://%s:%s", mockBackEnd.getHostName(),
                mockBackEnd.getPort());

        oauthRequester = oauthRequester.builder()
                .clientId("clientId")
                .secretId("secretId")
                .oauthUrl(baseUrl)
                .openApiUrl(baseUrl)
                .webclient(webClient)
                .build();
    }

    @Test
    @DisplayName("authorization code로 access token을 가져온다.")
    public void getAccessToken() throws RuntimeException, JsonProcessingException {
        //given
        String authorizationCode = "code";

//        String url = UriComponentsBuilder.fromUriString("https://github.com/login/oauth/access_token")
//                .queryParam("client_id", "clientId")
//                .queryParam("client_secret", "secretId")
//                .queryParam("code", authorizationCode)
//                .build()
//                .toUriString();

//        given(restTemplate.postForEntity(any(),any(), any()))
//                .willReturn(ResponseEntity.ok(expected));
        OauthAccessTokenResponse expected = OauthAccessTokenResponse.builder()
                .tokenType("bearer")
                .scope("")
                .accessToken("ACCESS_TOKEN")
                .build();

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(expected))
                .addHeader("Content-Type", "application/json"));

        String accessToken = oauthRequester.getAccessToken(authorizationCode);

        // then
        assertThat(accessToken).isEqualTo("ACCESS_TOKEN");
    }

    @Test
    @DisplayName("accessToken으로 userInformation을 가져온다.")
    void getUserInfo() throws JsonProcessingException {
        // given
        OauthUserInfoResponse expected = OauthUserInfoResponse.builder()
                .email("solver@test.com")
                .build();

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(expected))
                .addHeader("Content-Type", "application/json"));

        // when
        OauthUserInfoResponse oauthUserInfoResponse = oauthRequester.getUserInfo("ACCESS_TOKEN");

        // then
        assertThat(oauthUserInfoResponse.getEmail()).isEqualTo("solver@test.com");
    }


}