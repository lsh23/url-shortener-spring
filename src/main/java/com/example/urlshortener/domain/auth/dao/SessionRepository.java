package com.example.urlshortener.domain.auth.dao;

import com.example.urlshortener.domain.auth.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    boolean existsByUuid(String uuid);
    Optional<Session> findByUuid(String uuid);
}
