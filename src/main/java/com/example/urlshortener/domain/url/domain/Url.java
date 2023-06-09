package com.example.urlshortener.domain.url.domain;

import com.example.urlshortener.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Url {

    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "hash")
    private String hash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Column(name = "cookie_id")
    private String cookieId;

    @Builder
    private Url(String fullUrl, String hash) {
        this.fullUrl = fullUrl;
        this.hash = hash;
    }

    public static Url of(String fullUrl, String hash){
        return Url.builder()
                .fullUrl(fullUrl)
                .hash(hash)
                .build();
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getHash(){
        return hash;
    }
}
