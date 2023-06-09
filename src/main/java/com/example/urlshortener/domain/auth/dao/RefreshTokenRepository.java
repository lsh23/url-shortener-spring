package com.example.urlshortener.domain.auth.dao;

import com.example.urlshortener.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
    Optional<RefreshToken> findRefreshTokenByEmail(String email);
}
