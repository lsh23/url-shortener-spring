package com.example.urlshortener.domain.url.api;

import com.example.urlshortener.domain.url.application.UrlService;
import com.example.urlshortener.domain.url.dto.ShortenUrlForMeRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
import com.example.urlshortener.domain.url.dto.ShortenUrlsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/api/url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest shortenUrlRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.shortenUrl(shortenUrlRequest));
    }

    @GetMapping("/api/url")
    public ResponseEntity<ShortenUrlsResponse> findShortenUrl(@RequestParam Long memberId) {
        return ResponseEntity.ok(urlService.findAllByMemberId(memberId));
    }

    @PostMapping("/api/me/url")
    public ResponseEntity<ShortenUrlResponse> shortenUrlForMe(@RequestBody ShortenUrlForMeRequest shortenUrlForMeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.shortenUrlForMe(shortenUrlForMeRequest));
    }

    @GetMapping("/{hash}")
    public ResponseEntity<Void> redirect(@PathVariable String hash) throws URISyntaxException {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(urlService.redirect(hash))
                .build();
    }
}
