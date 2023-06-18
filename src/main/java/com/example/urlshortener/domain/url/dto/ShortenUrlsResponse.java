package com.example.urlshortener.domain.url.dto;

import com.example.urlshortener.domain.url.domain.Url;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenUrlsResponse {

    private List<ShortenUrlResponse> urls = new ArrayList<>();

    @Builder
    private ShortenUrlsResponse(List<ShortenUrlResponse> urls) {
        this.urls = urls;
    }

    public static ShortenUrlsResponse of(List<Url> urlList) {
        List<ShortenUrlResponse> urls = urlList.stream().map(url -> ShortenUrlResponse.of(url)).collect(Collectors.toList());
        return ShortenUrlsResponse.builder().urls(urls).build();
    }

}
