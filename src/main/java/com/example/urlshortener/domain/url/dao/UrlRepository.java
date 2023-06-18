package com.example.urlshortener.domain.url.dao;

import com.example.urlshortener.domain.url.domain.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByHash(String hash);
    List<Url> findAllByMemberId(Long memberId);
}
