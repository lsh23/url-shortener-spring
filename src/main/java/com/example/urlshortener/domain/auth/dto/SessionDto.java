package com.example.urlshortener.domain.auth.dto;

import com.example.urlshortener.domain.auth.domain.Session;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionDto {

    private String uuid;

    @Builder
    private SessionDto(String uuid) {
        this.uuid = uuid;
    }

    public static SessionDto of(Session session) {
        return SessionDto.builder().uuid(session.getUUID()).build();
    }

    public ResponseCookie toCookie(){
        return ResponseCookie.from("sessionId", this.uuid)
                .httpOnly(true)
                .secure(true)
                .maxAge(86400 * 7)
                .build();
    }
}
