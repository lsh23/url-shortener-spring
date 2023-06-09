package com.example.urlshortener.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshAuthResponse {
    @JsonProperty("access_token")
    private String accessToken;


    @Builder
    public RefreshAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }


    public static RefreshAuthResponse of(String accessToken) {
        return RefreshAuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
