package com.example.urlshortener.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginRequest {

    private String provider;
    private String code;

    @Builder
    public OAuthLoginRequest(String provider, String code) {
        this.provider = provider;
        this.code = code;
    }
}
