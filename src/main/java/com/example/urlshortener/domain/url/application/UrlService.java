package com.example.urlshortener.domain.url.application;

import com.example.urlshortener.domain.auth.dao.SessionRepository;
import com.example.urlshortener.domain.auth.domain.Session;
import com.example.urlshortener.domain.auth.exception.SessionNotFoundException;
import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.domain.Url;
import com.example.urlshortener.domain.url.dto.*;
import com.example.urlshortener.domain.url.exception.HashNotFoundException;
import com.example.urlshortener.domain.url.exception.UrlExpiredException;
import com.example.urlshortener.domain.url.exception.UrlNotMatchedByMember;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final MemberRepository memberRepository;

    private final SessionRepository sessionRepository;

    @Transactional
    public ShortenUrlResponse shortenUrl(ShortenUrlRequest shortenUrlRequest) {
        String fullUrl = shortenUrlRequest.getFullUrl();
        String hash = UrlShortener.shortenUrl(fullUrl);
        String sessionUuid = shortenUrlRequest.getSessionUuid();

        Url url = Url.of(fullUrl, hash);

        Session session = sessionRepository.findByUuid(sessionUuid)
                .orElseThrow(() -> new SessionNotFoundException());

        url.assignSession(session);

        urlRepository.save(url);
        return ShortenUrlResponse.of(url);
    }

    @Transactional
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

    public ShortenUrlsResponse findAllByMemberId(Long memberId) {
        List<Url> allByMemberId = urlRepository.findAllByMemberId(memberId);
        return ShortenUrlsResponse.of(allByMemberId);
    }

    @Transactional
    public void update(String hash, ShortenUrlUpdateRequest request) {

        Url url = urlRepository.findByHash(hash)
                .orElseThrow(() -> new HashNotFoundException());

        if (url.getMember().getId() != request.getMemberId()){
            throw new UrlNotMatchedByMember();
        }
        url.extendExpireTime(request.getExpireAt());
    }

    @Transactional
    public void expire(String hash, ShortenUrlUpdateRequest request) {
        Url url = urlRepository.findByHash(hash)
                .orElseThrow(() -> new HashNotFoundException());

        if (url.getMember().getId() != request.getMemberId()){
            throw new UrlNotMatchedByMember();
        }
        url.expire(request.getExpireAt());
    }

    @Transactional
    public void delete(String hash, ShortenUrlDeleteRequest request) {
        Url url = urlRepository.findByHash(hash)
                .orElseThrow(() -> new HashNotFoundException());

        if (url.getMember().getId() != request.getMemberId()){
            throw new UrlNotMatchedByMember();
        }
        urlRepository.delete(url);
    }

    public ShortenUrlsResponse findAllBySessionUuid(String sessionUuid) {
        List<Url> allByMemberId = urlRepository.findAllBySessionUuid(sessionUuid);
        return ShortenUrlsResponse.of(allByMemberId);
    }
}
