package com.example.urlshortener.domain.url.application;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.domain.Url;
import com.example.urlshortener.domain.url.dto.ShortenUrlForMeRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
import com.example.urlshortener.domain.url.exception.UrlExpiredException;
import com.example.urlshortener.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

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
    void shortenUrl(){
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
    void shortenUrlForMe(){
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
        assertThatThrownBy(()->urlService.redirect(UrlShortener.shortenUrl(url))).isInstanceOf(UrlExpiredException.class);


    }
}