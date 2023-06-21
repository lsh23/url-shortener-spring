package com.example.urlshortener.domain.url.dto;

import lombok.*;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenUrlForMeRequest {
    private Long memberId;
    private String fullUrl;

    @Builder
    private ShortenUrlForMeRequest(String fullUrl, Long memberId) {
        this.fullUrl = fullUrl;
        this.memberId = memberId;
    }

}
