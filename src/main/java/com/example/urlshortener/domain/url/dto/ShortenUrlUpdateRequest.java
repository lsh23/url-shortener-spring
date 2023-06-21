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
public class ShortenUrlUpdateRequest {

    @NotNull
    private Long memberId;

    @Nullable
    private LocalDateTime expireAt;

    @Builder
    private ShortenUrlUpdateRequest(Long memberId, LocalDateTime expireAt) {
        this.memberId = memberId;
        this.expireAt = expireAt;
    }
}
