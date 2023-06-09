package com.example.urlshortener.domain.url.application;

import com.example.urlshortener.domain.url.dao.UrlRepository;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
import com.example.urlshortener.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UrlServiceTest extends IntegrationTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @BeforeEach
    public void setUp() throws Exception {
        urlRepository.deleteAllInBatch();
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

}