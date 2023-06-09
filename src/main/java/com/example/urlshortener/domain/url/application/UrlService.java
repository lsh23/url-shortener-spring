package com.example.urlshortener.domain.url.application;

import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.domain.Url;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    public ShortenUrlResponse shortenUrl(ShortenUrlRequest shortenUrlRequest) {
        String fullUrl = shortenUrlRequest.getFullUrl();
        String hash = UrlShortener.shortenUrl(fullUrl);

        Url url = urlRepository.save(Url.of(fullUrl, hash));

        return ShortenUrlResponse.of(url);
    }
}
