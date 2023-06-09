package com.example.urlshortener.domain.auth.dto;

import com.example.urlshortener.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicLoginResponse {
    private String email;
    private String accessToken;
    private String refreshToken;

    @Builder
    public BasicLoginResponse(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static BasicLoginResponse of(Member member, String token, String refreshToken) {
        return BasicLoginResponse.builder()
                .email(member.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
