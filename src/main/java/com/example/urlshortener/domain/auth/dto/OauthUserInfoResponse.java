package com.example.urlshortener.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OauthUserInfoResponse {
    private String email;

    protected OauthUserInfoResponse(){
    }
    @Builder
    private OauthUserInfoResponse(String email) {
        this.email = email;
    }

    public static OauthUserInfoResponse from(Map<String, Object> responseBody){
        final String email = responseBody.get("email").toString();
        return OauthUserInfoResponse.builder().email(email).build();
    }
}
