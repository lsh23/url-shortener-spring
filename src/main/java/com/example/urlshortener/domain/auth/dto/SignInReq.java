package com.example.urlshortener.domain.auth.dto;

import com.example.urlshortener.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInReq {
    private String email;
    private String password;

    @Builder
    public SignInReq(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}