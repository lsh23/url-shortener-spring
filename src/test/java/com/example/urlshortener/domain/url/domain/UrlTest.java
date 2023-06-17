package com.example.urlshortener.domain.url.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    private static Stream<Arguments> provideLocalDateTimesForCheckExpired(){
        return Stream.of(
                Arguments.of(LocalDateTime.of(2023, 5, 30, 0, 0), false),
                Arguments.of(LocalDateTime.of(2023, 5, 29, 23, 59), false),
                Arguments.of(LocalDateTime.of(2023, 5, 31, 0, 1), true)
        );
    }

}