package com.example.urlshortener.domain.url.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerTest {
    @Test
    @DisplayName("URL을 압축한다")
    void test(){
        // given when
        String hash = UrlShortener.shortenUrl("www.test.com");

        // then
        assertThat(hash).isEqualTo("84cc0e5c525dc728");
    }
    
}