package com.example.urlshortener.domain.url.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenUrlDeleteRequest {

    @NotNull
    private Long memberId;

    @Builder
    private ShortenUrlDeleteRequest(Long memberId, LocalDateTime expireAt) {
        this.memberId = memberId;
    }
}
