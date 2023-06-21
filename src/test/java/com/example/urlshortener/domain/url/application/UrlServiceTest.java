package com.example.urlshortener.domain.url.application;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.domain.Url;
import com.example.urlshortener.domain.url.dto.*;
import com.example.urlshortener.domain.url.exception.UrlExpiredException;
import com.example.urlshortener.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlServiceTest extends IntegrationTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    public void setUp() throws Exception {
        urlRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("full url을 가지고 hash값을 생성하고 저장한다.")
    void shortenUrl() {
        // given
        ShortenUrlRequest request = ShortenUrlRequest.builder()
                .fullUrl("www.test.com")
                .build();

        // when
        ShortenUrlResponse response = urlService.shortenUrl(request);

        // then
        assertThat(response.getHash()).isNotBlank();
    }

    @Test
    @DisplayName("full url와 memberId을 가지고 hash값을 생성하고 저장한다.")
    void shortenUrlForMe() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        ShortenUrlForMeRequest request = ShortenUrlForMeRequest.builder()
                .memberId(member.getId())
                .fullUrl("www.test.com")
                .build();

        // when
        ShortenUrlResponse response = urlService.shortenUrlForMe(request);

        // then
        assertThat(response.getHash()).isNotBlank();
    }

    @Test
    @DisplayName("hash값으로 redirect할 url을 조회한다.")
    void redirect() throws URISyntaxException {
        // given
        String url = "www.test.com";
        Url savedUrl = Url.builder()
                .fullUrl(url)
                .hash(UrlShortener.shortenUrl(url))
                .expiredAt(LocalDateTime.now().plusHours(1))
                .build();
        urlRepository.save(savedUrl);

        // when
        URI redirect = urlService.redirect(UrlShortener.shortenUrl(url));

        // then
        assertThat(redirect.getPath()).isEqualTo("www.test.com");
    }

    @Test
    @DisplayName("만료된 url을 조회하면 예외를 던진다")
    void redirectExpiredURL() throws URISyntaxException {
        // given
        String url = "www.test.com";
        Url savedUrl = Url.builder()
                .fullUrl(url)
                .hash(UrlShortener.shortenUrl(url))
                .expiredAt(LocalDateTime.now().minusHours(1))
                .build();
        urlRepository.save(savedUrl);

        // when // then
        assertThatThrownBy(() -> urlService.redirect(UrlShortener.shortenUrl(url))).isInstanceOf(UrlExpiredException.class);
    }

    @Test
    @DisplayName("member id로 url을 조회한다.")
    void findAllByMemberId() throws URISyntaxException {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        String url = "www.test.com";
        Url savedUrl = Url.builder()
                .fullUrl(url)
                .hash(UrlShortener.shortenUrl(url))
                .expiredAt(LocalDateTime.now().minusHours(1))
                .build();
        savedUrl.assignMember(member);
        urlRepository.save(savedUrl);


        // when
        ShortenUrlsResponse response = urlService.findAllByMemberId(member.getId());

        // then
        assertThat(response.getUrls().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("url의 만료기간을 연장한다.")
    void prolongShortUrlExpiration() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        String url = "www.test.com";
        String hash = UrlShortener.shortenUrl(url);
        LocalDateTime expireAt = LocalDateTime.of(2023,6,1,0,0);
        Url savedUrl = Url.builder()
                .fullUrl(url)
                .hash(hash)
                .expiredAt(expireAt)
                .build();
        savedUrl.assignMember(member);
        urlRepository.save(savedUrl);

        ShortenUrlUpdateRequest request = ShortenUrlUpdateRequest.builder()
                .memberId(member.getId())
                .expireAt(expireAt.plusDays(7))
                .build();

        // when
        urlService.update(hash, request);

        // then
        Url actual = urlRepository.findByHash(hash).get();
        assertThat(actual.getExpiredAt()).isEqualTo(expireAt.plusDays(7));
    }

    @Test
    @DisplayName("url의 만료시각을 현재시간으로 만든다.")
    void expire() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        String url = "www.test.com";
        String hash = UrlShortener.shortenUrl(url);
        LocalDateTime expireAt = LocalDateTime.of(2023,6,1,0,0);
        Url savedUrl = Url.builder()
                .fullUrl(url)
                .hash(hash)
                .expiredAt(expireAt)
                .build();
        savedUrl.assignMember(member);
        urlRepository.save(savedUrl);

        LocalDateTime now = LocalDateTime.of(2023, 6, 7, 0, 0);
        ShortenUrlUpdateRequest request = ShortenUrlUpdateRequest.builder()
                .memberId(member.getId())
                .expireAt(now)
                .build();

        // when
        urlService.expire(hash, request);

        // then
        Url actual = urlRepository.findByHash(hash).get();
        assertThat(actual.getExpiredAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("url을 삭제한다.")
    void delete() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        String url = "www.test.com";
        String hash = UrlShortener.shortenUrl(url);
        LocalDateTime expireAt = LocalDateTime.of(2023,6,1,0,0);
        Url savedUrl = Url.builder()
                .fullUrl(url)
                .hash(hash)
                .expiredAt(expireAt)
                .build();
        savedUrl.assignMember(member);
        urlRepository.save(savedUrl);

        ShortenUrlDeleteRequest request = ShortenUrlDeleteRequest.builder()
                .memberId(member.getId())
                .build();

        // when
        urlService.delete(hash, request);

        // then
        assertThatThrownBy(()->urlRepository.findByHash(hash).orElseThrow()).isInstanceOf(NoSuchElementException.class);
    }


}