package com.example.urlshortener.domain.auth.dto;

import com.example.urlshortener.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginResponse {

    private Long id;
    private String email;
    private String accessToken;
    private String refreshToken;
    @Builder
    public OAuthLoginResponse(Long id, String email, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public static OAuthLoginResponse of (Member member, String token, String refreshToken){
        return OAuthLoginResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
