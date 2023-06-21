package com.example.urlshortener.domain.url.domain;

import com.example.urlshortener.domain.url.exception.InvalidProlongExpirationPeriodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlTest {

    @ParameterizedTest
    @MethodSource("provideLocalDateTimesForCheckExpired") // needs to match an existing method.
    @DisplayName("2023년 5월 30일 0시 0분이 만료인, url의 만료 여부를 확인한다.")
    void checkExpired(LocalDateTime now, boolean expected) {
        // given
        Url url = Url.builder().expiredAt(LocalDateTime.of(2023, 5, 30, 0, 0))
                .build();

        // when
        boolean expired = url.checkExpired(now);

        // then
        assertThat(expired).isEqualTo(expected);
    }

    @Test
    @DisplayName("원하는 날짜로 url의 만료날짜를 지정한다.")
    void extendExpireTime() {
        // given
        LocalDateTime expiredAt = LocalDateTime.of(2023, 6, 1, 0, 0);
        Url url = Url.builder().expiredAt(expiredAt)
                .build();

        // when
        url.extendExpireTime(expiredAt.plusDays(7));

        // then
        assertThat(url.getExpiredAt()).isEqualTo(LocalDateTime.of(2023, 6, 8, 0, 0));
    }

    @Test
    @DisplayName("기존 만료날짜보다 이전시간으로 만료날짜로 지정하면, 에러를 던진다.")
    void extendExpireTimeWithInvalidExpiredAt() {
        // given
        LocalDateTime expiredAt = LocalDateTime.of(2023, 6, 1, 0, 0);
        Url url = Url.builder().expiredAt(expiredAt)
                .build();

        // when & then
        assertThatThrownBy( () -> url.extendExpireTime(expiredAt.minusDays(7)))
                .isInstanceOf(InvalidProlongExpirationPeriodException.class);
    }


    private static Stream<Arguments> provideLocalDateTimesForCheckExpired() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2023, 5, 30, 0, 0), false),
                Arguments.of(LocalDateTime.of(2023, 5, 29, 23, 59), false),
                Arguments.of(LocalDateTime.of(2023, 5, 31, 0, 1), true)
        );
    }

}