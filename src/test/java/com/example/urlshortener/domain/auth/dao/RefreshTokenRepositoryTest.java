package com.example.urlshortener.domain.auth.dao;

import com.example.urlshortener.domain.auth.domain.RefreshToken;
import com.example.urlshortener.test.RedisRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RefreshTokenRepositoryTest extends RedisRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Test
    @DisplayName("이메일로 refresh token을 조회한다.")
    void findRefreshTokenByEmail(){
        // given
        String email = "solver@test.com";
        String token = "token";
        RefreshToken refreshToken = RefreshToken.builder()
                .email(email)
                .token(token)
                .build();
        refreshTokenRepository.save(refreshToken);

        // when
        RefreshToken expected = refreshTokenRepository.findRefreshTokenByEmail(email).orElseThrow();

        // then
        assertThat(expected.getEmail()).isEqualTo("solver@test.com");
    }

    @Test
    @DisplayName("유효하지 않은 이메일로 refresh token을 조회하면 예외를 던진다.")
    void findRefreshTokenByEmailWithInvalidEmail(){
        // given
        String email = "solver@test.com";
        String token = "token";
        RefreshToken refreshToken = RefreshToken.builder()
                .email(email)
                .token(token)
                .build();
        refreshTokenRepository.save(refreshToken);

        // when // then
        assertThatThrownBy(()->refreshTokenRepository.findRefreshTokenByEmail("invalid@test.com").orElseThrow()).isInstanceOf(
                NoSuchElementException.class
        );

    }

}