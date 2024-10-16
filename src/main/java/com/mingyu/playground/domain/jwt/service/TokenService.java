package com.mingyu.playground.domain.jwt.service;

import com.mingyu.playground.common.error.PlayGroundCommonException;
import com.mingyu.playground.common.error.PlayGroundErrorCode;
import com.mingyu.playground.domain.jwt.entities.Token;
import com.mingyu.playground.domain.jwt.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * access token으로 Token 데이터를 가져와 삭제하는 메소드
     *
     * @param token
     */
    @Transactional
    public void deleteByAccessToken(String token) {
        tokenRepository.findByAccessToken(token).ifPresent(tokenRepository::delete);
    }

    /**
     * access token으로 Token 데이터를 가져오는 메소드
     *
     * @param token access token
     * @return Token 데이터
     */
    @Transactional(readOnly = true)
    public Optional<Token> findByAccessToken(String token) {
        return tokenRepository.findByAccessToken(token);
    }

    /**
     * refresh token으로 Token 데이터를 가져오는 메소드
     *
     * @param token refresh token
     * @return Token 데이터
     */
    @Transactional(readOnly = true)
    public Optional<Token> findByRefreshToken(String token) {
        return tokenRepository.findByRefreshToken(token);
    }

    /**
     * refresh token으로 Token 데이터를 가져와 access token 값을 업데이트하는 메소드
     *
     * @param refreshToken
     * @param accessToken
     */
    @Transactional
    public void updateByRefreshToken(String refreshToken, String accessToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new PlayGroundCommonException(PlayGroundErrorCode.JWT_TOKEN_NOT_FOUND.getCode(), PlayGroundErrorCode.JWT_TOKEN_NOT_FOUND.getMessage()));

        token = token.update(accessToken);
        tokenRepository.save(token);
    }

}
