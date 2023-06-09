package com.example.urlshortener.domain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OauthAccessTokenResponse {


    protected OauthAccessTokenResponse(){

    }
    private String accessToken;
    private String tokenType;
    private String scope;

    @Builder
    private OauthAccessTokenResponse(final String accessToken, final String tokenType, final String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }
}