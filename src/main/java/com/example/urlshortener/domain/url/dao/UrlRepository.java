package com.example.urlshortener.domain.url.dao;

import com.example.urlshortener.domain.url.domain.Url;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UrlRepository extends JpaRepository<Url, Long> {
}
