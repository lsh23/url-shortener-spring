package com.example.urlshortener.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshAuthRequest {

    private String email;
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Builder
    public RefreshAuthRequest(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

}
