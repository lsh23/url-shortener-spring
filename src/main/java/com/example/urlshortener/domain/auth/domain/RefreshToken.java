package com.example.urlshortener.domain.auth.domain;

import com.example.urlshortener.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@Getter
public class RefreshToken {
    @Id
    private Long id;

    private String token;

    @Indexed
    private String email;

    @Builder
    private RefreshToken(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public static RefreshToken of(Member member, String refreshToken) {
        return RefreshToken.builder()
                .token(refreshToken)
                .email(member.getEmail())
                .build();
    }
}
