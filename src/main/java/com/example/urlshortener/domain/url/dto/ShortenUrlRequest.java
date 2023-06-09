package com.example.urlshortener.domain.url.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenUrlRequest {

    private String fullUrl;

    @Builder
    private ShortenUrlRequest(String fullUrl) {
        this.fullUrl = fullUrl;
    }

}
