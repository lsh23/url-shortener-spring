package com.example.urlshortener.domain.url.dto;

import com.example.urlshortener.domain.url.domain.Url;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenUrlResponse {

    private String fullUrl;
    private String hash;


    @Builder
    private ShortenUrlResponse(String fullUrl, String hash) {
        this.fullUrl = fullUrl;
        this.hash = hash;
    }

    public static ShortenUrlResponse of(Url url) {
        return ShortenUrlResponse.builder()
                .fullUrl(url.getFullUrl())
                .hash(url.getHash())
                .build();
    }
}
