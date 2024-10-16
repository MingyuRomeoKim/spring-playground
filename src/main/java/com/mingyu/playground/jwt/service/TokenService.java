package com.mingyu.playground.jwt.service;

import com.mingyu.playground.jwt.entities.Token;
import com.mingyu.playground.jwt.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    /**
     * 토큰 사용자의 토큰이 저장되어 있을 경우 update, 없을 경우에는 create
     */
    @Transactional
    public void saveOrUpdate(Token token) {
        Token savedToken = tokenRepository.findByMemberId(token.getMember().getId());

        if (savedToken == null) {
            tokenRepository.save(token);
        } else {
            savedToken.update(token.getAccessToken(), token.getRefreshToken(), token.getGrantType());
            tokenRepository.save(savedToken);
        }
    }

}
