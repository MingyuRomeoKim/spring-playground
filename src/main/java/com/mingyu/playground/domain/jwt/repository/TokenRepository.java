package com.mingyu.playground.domain.jwt.repository;

import com.mingyu.playground.domain.jwt.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Token findByMemberId(UUID memberId);
    Token findByMemberEmail(String email);

    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByRefreshToken(String refreshToken);

}
