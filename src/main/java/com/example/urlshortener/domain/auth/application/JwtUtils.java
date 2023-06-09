package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.exception.InvalidTokenException;
import com.example.urlshortener.domain.auth.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


@Component
public class JwtUtils {
    private final String secretKey;
    private final String refreshSecretKey;
    private final long validityInMilliseconds;
    private final long refreshValidityInMilliseconds;
    private final JwtParser jwtParser;
    private final JwtParser refreshJwtParser;

    @Builder
    private JwtUtils(@Value("${spring.jwt.token.secret-key}") String secretKey,
                    @Value("${spring.jwt.token.refresh-secret-key}") String refreshSecretKey,
                    @Value("${spring.jwt.token.expire-length}") long validityInMilliseconds,
                    @Value("${spring.jwt.token.expire-length}") long refreshValidityInMilliseconds) {
        this.secretKey = secretKey;
        this.refreshSecretKey = refreshSecretKey;
        this.validityInMilliseconds = validityInMilliseconds;
        this.refreshValidityInMilliseconds = refreshValidityInMilliseconds;
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
        this.refreshJwtParser = Jwts.parser().setSigningKey(refreshSecretKey);
    }

    public String createToken(Map<String, Object> payload, Date from) {
        Claims claims = Jwts.claims(payload);
        Date validity = new Date(from.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(from)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(Map<String, Object> payload, Date from) {
        Claims claims = Jwts.claims(payload);
        Date validity = new Date(from.getTime() + refreshValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(from)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    public void validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            refreshJwtParser.parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public String getPayload(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public static PayloadBuilder payloadBuilder() {
        return new PayloadBuilder();
    }

    public static class PayloadBuilder {
        private final Claims claims;
        private final String USER_EMAIL = "email";

        private PayloadBuilder() {
            this.claims = Jwts.claims();
        }

        public PayloadBuilder setUserEmail(String email) {
            claims.put(USER_EMAIL,email);
            return this;
        }

        public Map<String, Object> build() {
            return claims;
        }
    }

}