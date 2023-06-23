package com.example.urlshortener.domain.url.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenUrlRequest {

    @NotBlank
    private String fullUrl;

    @Nullable
    private String sessionUuid;

    @Builder
    private ShortenUrlRequest(String fullUrl, String sessionUuid) {
        this.fullUrl = fullUrl;
        this.sessionUuid = sessionUuid;
    }
}
