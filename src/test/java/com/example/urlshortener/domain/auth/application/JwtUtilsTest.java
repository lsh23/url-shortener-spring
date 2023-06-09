package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.exception.InvalidTokenException;
import com.example.urlshortener.domain.auth.exception.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class JwtUtilsTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final int INDEX_OF_HEADER = 0;
    private static final int INDEX_OF_PAYLOAD = 1;
    private static final int INDEX_OF_SIGNATURE = 2;

    private JwtUtils jwtUtils = JwtUtils.builder()
            .secretKey("testjwtsecretkeyforaccesstokentestjwtsecretkeyforaccesstoken")
            .refreshSecretKey("testjwtsecretkeyforrefreshtokentestjwtsecretkeyforrefreshtoken")
            .validityInMilliseconds(1800000)
            .refreshValidityInMilliseconds(259200000)
            .build();

    @Test
    @DisplayName("access token을 생성한다.")
    void createToken() {
        // given
        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail("solver@test.com")
                .build();

        // when
        String token = jwtUtils.createToken(payload, new Date());

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("refresh token을 생성한다.")
    void createRefreshToken() {
        // given
        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail("solver@test.com")
                .build();

        // when
        String refreshToken = jwtUtils.createRefreshToken(payload, new Date());

        // then
        assertThat(refreshToken).isNotBlank();
    }

    @Test
    @DisplayName("token이 만료되었으면 예외를 던진다.")
    void validateTokenWithExpiredToken() {
        // given
        LocalDateTime issuedAtDateTime = LocalDateTime.of(2023, Month.MAY, 1, 06, 30);
        Date issuedAt = Timestamp.valueOf(issuedAtDateTime);

        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail("solver@test.com")
                .build();

        String token = jwtUtils.createToken(payload, issuedAt);
        // when then
        assertThatThrownBy(() -> jwtUtils.validateToken(token)).isInstanceOf(TokenExpiredException.class);
    }

    @Test
    @DisplayName("refresh token이 만료되었으면 예외를 던진다.")
    void validateRefreshTokenWithExpiredToken() {
        // given
        LocalDateTime issuedAtDateTime = LocalDateTime.of(2023, Month.MAY, 1, 06, 30);
        Date issuedAt = Timestamp.valueOf(issuedAtDateTime);

        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail("solver@test.com")
                .build();

        String refreshToken = jwtUtils.createRefreshToken(payload, issuedAt);
        // when then
        assertThatThrownBy(() -> jwtUtils.validateRefreshToken(refreshToken)).isInstanceOf(TokenExpiredException.class);
    }

    @Test
    @DisplayName("token이 조작되었으면 예외를 던진다.")
    void validateTokenWithInvalidToken() throws JsonProcessingException {
        // given
        LocalDateTime issuedAtDateTime = LocalDateTime.of(2023, Month.MAY, 1, 06, 30);
        Date issuedAt = Timestamp.valueOf(issuedAtDateTime);

        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail("solver@test.com")
                .build();

        String token = jwtUtils.createToken(payload, new Date());
        String manipulatedToken = manipulateToken(token);

        // when then
        assertThatThrownBy(() -> jwtUtils.validateToken(manipulatedToken)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("refresh token이 조작되었으면 예외를 던진다.")
    void validateRefreshTokenWithInvalidToken() throws JsonProcessingException {
        // given
        LocalDateTime issuedAtDateTime = LocalDateTime.of(2023, Month.MAY, 1, 06, 30);
        Date issuedAt = Timestamp.valueOf(issuedAtDateTime);

        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail("solver@test.com")
                .build();

        String refreshToken = jwtUtils.createRefreshToken(payload, issuedAt);
        String manipulatedRefreshToken = manipulateToken(refreshToken);

        // when then
        assertThatThrownBy(() -> jwtUtils.validateRefreshToken(manipulatedRefreshToken)).isInstanceOf(InvalidTokenException.class);
    }

    private String manipulateToken(String token) throws JsonProcessingException {

        String[] originJwt = token.split("\\.");
        String encodedPayload = originJwt[INDEX_OF_PAYLOAD];

        Base64.Decoder decoder = Base64.getDecoder();
        String decodedPayload = new String(decoder.decode(encodedPayload));

        Map map = objectMapper.readValue(decodedPayload, Map.class);
        map.merge("exp", 1000, (oldValue, value) -> (Integer) oldValue + (Integer) value);

        Base64.Encoder encoder = Base64.getEncoder();
        String manipulatedPayload = new String(encoder.encode(objectMapper.writeValueAsString(map).getBytes()));

        return String.join("\\.", Arrays.asList(originJwt[INDEX_OF_HEADER], manipulatedPayload, originJwt[INDEX_OF_SIGNATURE]));
    }
}