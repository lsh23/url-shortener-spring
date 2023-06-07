package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dto.OauthAccessTokenResponse;
import com.example.urlshortener.domain.auth.dto.OauthUserInfoResponse;
import com.example.urlshortener.domain.auth.exception.FetchAccessTokenException;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@PropertySource("classpath:config/oauth.properties")
public class OauthRequester {
    private final String clientId;
    private final String secretId;
    private final String oauthUrl;
    private final String openApiUrl;
    private final WebClient oauthLoginClient;
    private final WebClient openApiClient;

    @Builder
    private OauthRequester(
            @Value("${client-id}") final String clientId,
            @Value("${secret-id}") final String secretId,
            @Value("${oauth-url}") final String oauthUrl,
            @Value("${openapi-url}") final String openApiUrl,
            final WebClient webclient
    ){
        this.clientId = clientId;
        this.secretId = secretId;
        this.oauthUrl = oauthUrl;
        this.openApiUrl = openApiUrl;
        this.oauthLoginClient = oauthLoginClient(webclient, oauthUrl);
        this.openApiClient = openApiClient(webclient,openApiUrl);
    }

//    public String getAccessToken(String code) {
//        String url = UriComponentsBuilder.fromUriString("https://github.com/login/oauth/access_token")
//                .queryParam("client_id", clientId)
//                .queryParam("client_secret", secretId)
//                .queryParam("code", code)
//                .build()
//                .toUriString();
//        ResponseEntity<OauthAccessTokenResponse> githubAccessTokenResponseResponseEntity = restTemplate.postForEntity(url, null, OauthAccessTokenResponse.class);
//        OauthAccessTokenResponse body = githubAccessTokenResponseResponseEntity.getBody();
//        return body.getAccessToken();
//    }
//
//    public OauthUserInfoResponse getUserInfo(String accessToken) {
//        String url = UriComponentsBuilder.fromUriString("https://api.github.com/user")
//                .build()
//                .toUriString();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer "+accessToken);
//        HttpEntity<String> entity = new HttpEntity<String>("", headers);
//        ResponseEntity<OauthUserInfoResponse> githubUserInfoResponseResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, OauthUserInfoResponse.class);
//        OauthUserInfoResponse oauthUserInfoResponse = githubUserInfoResponseResponseEntity.getBody();
//        return oauthUserInfoResponse;
//    }

    public String getAccessToken(String code){
        Map<String, Object> responseBody = oauthLoginClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("code", code)
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", secretId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .blockOptional()
                .orElseThrow();
        validateResponseBody(responseBody);
        return responseBody.get("access_token").toString();
    }

    private void validateResponseBody(Map<String, Object> responseBody) {
        if (!responseBody.containsKey("access_token")) {
            throw new FetchAccessTokenException(responseBody.get("error_description").toString());
        }
    }

    public OauthUserInfoResponse getUserInfo(String accessToken){
        Map<String, Object> responseBody = openApiClient.get()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .blockOptional()
                .orElseThrow();
        return OauthUserInfoResponse.from(responseBody);
    }
    private WebClient oauthLoginClient(final WebClient webClient, String oauthUrl){
        return webClient.mutate()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(oauthUrl)
                .build();
    }

    private WebClient openApiClient(final WebClient webClient, String openApiUrl){
        return webClient.mutate()
                .baseUrl(openApiUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
