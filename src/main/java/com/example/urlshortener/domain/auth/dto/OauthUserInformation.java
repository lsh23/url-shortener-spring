package com.example.urlshortener.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthUserInformation {
    private String email;
    @Builder
    public OauthUserInformation(String email) {
        this.email = email;
    }

    public static OauthUserInformation of(OauthUserInfoResponse userInfoResponse) {
        return OauthUserInformation.builder().email(userInfoResponse.getEmail()).build();
    }
}

