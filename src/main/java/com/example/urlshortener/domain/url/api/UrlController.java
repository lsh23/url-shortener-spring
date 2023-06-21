package com.example.urlshortener.domain.url.api;

import com.example.urlshortener.domain.url.application.UrlService;
import com.example.urlshortener.domain.url.dto.*;
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

    @PutMapping("/api/me/url/{hash}")
    public ResponseEntity<Void> update(@PathVariable String hash, @RequestBody ShortenUrlUpdateRequest request) {
        urlService.update(hash, request);
        return ResponseEntity.ok().build();
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
