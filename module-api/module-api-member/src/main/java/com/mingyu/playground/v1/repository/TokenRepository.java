package com.mingyu.playground.v1.repository;


import com.mingyu.playground.entity.member.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Token findByMemberId(UUID memberId);
    Token findByMemberEmail(String email);

    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByRefreshToken(String refreshToken);

}
