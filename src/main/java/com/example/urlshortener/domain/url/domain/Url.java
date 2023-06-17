package com.example.urlshortener.domain.url.domain;

import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.global.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Url extends BaseTimeEntity {

    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "hash", unique = true, nullable = false)
    private String hash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "cookie_id")
    private String cookieId;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Builder
    private Url(String fullUrl, String hash, LocalDateTime expiredAt) {
        this.fullUrl = fullUrl;
        this.hash = hash;
        this.expiredAt = expiredAt;
    }

    public static Url of(String fullUrl, String hash) {
        return Url.builder()
                .fullUrl(fullUrl)
                .hash(hash)
                .expiredAt(LocalDateTime.now().plusDays(7))
                .build();
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getHash() {
        return hash;
    }

    public boolean checkExpired(LocalDateTime now) {
        if (now.isAfter(expiredAt)) {
            return true;
        }
        return false;
    }
}
