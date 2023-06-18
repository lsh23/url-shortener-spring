package com.example.urlshortener.domain.url.application;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.domain.Url;
import com.example.urlshortener.domain.url.dto.ShortenUrlForMeRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
import com.example.urlshortener.domain.url.exception.HashNotFoundException;
import com.example.urlshortener.domain.url.exception.UrlExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final MemberRepository memberRepository;

    public ShortenUrlResponse shortenUrl(ShortenUrlRequest shortenUrlRequest) {
        String fullUrl = shortenUrlRequest.getFullUrl();
        String hash = UrlShortener.shortenUrl(fullUrl);

        Url url = urlRepository.save(Url.of(fullUrl, hash));

        return ShortenUrlResponse.of(url);
    }

    public ShortenUrlResponse shortenUrlForMe(ShortenUrlForMeRequest shortenUrlForMeRequest) {
        String fullUrl = shortenUrlForMeRequest.getFullUrl();
        String hash = UrlShortener.shortenUrl(fullUrl);

        Url url = Url.of(fullUrl, hash);

        Long memberId = shortenUrlForMeRequest.getMemberId();

        Member member = memberRepository.findById(memberId).get();
        url.assignMember(member);

        urlRepository.save(url);

        return ShortenUrlResponse.of(url);
    }

    public URI redirect(String hash) throws URISyntaxException {
        Url url = urlRepository.findByHash(hash)
                .orElseThrow(() -> new HashNotFoundException());

        if (url.checkExpired(LocalDateTime.now())) {
            throw new UrlExpiredException();
        }

        return new URI(url.getFullUrl());
    }
}
