package com.mingyu.playground.util;

import com.mingyu.playground.errors.PlayGroundCommonException;
import com.mingyu.playground.errors.PlayGroundErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtTokenizer {

    private static String  accessSecret;
    private static String refreshSecret;
    public static Long accessTokenExpire;
    public static Long refreshTokenExpire;

    @Value("${jwt.access.secret}")
    private String  accessSecretValue;
    @Value("${jwt.refresh.secret}")
    private String refreshSecretValue;
    @Value("${jwt.access.expire}")
    public Long accessTokenExpireValue;
    @Value("${jwt.refresh.expire}")
    public Long refreshTokenExpireValue;

    @PostConstruct
    public void init() {
        accessSecret = accessSecretValue;
        refreshSecret = refreshSecretValue;
        accessTokenExpire = accessTokenExpireValue;
        refreshTokenExpire = refreshTokenExpireValue;
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, getSecretByte(accessSecret));
    }

    public Claims parseToken(String token, byte[] secretKey) {
        Claims claims = null;
        try {

            claims = Jwts.parser()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (SignatureException e) { // 토큰 유효성 체크 실패 시
            throw new PlayGroundCommonException(PlayGroundErrorCode.JWT_INVALID_ERROR.getCode(), PlayGroundErrorCode.JWT_INVALID_ERROR.getMessage());
        }

        return claims;
    }

    /**
     * @param secretKey - byte형식
     * @return Key 형식 시크릿 키
     */
    public static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }

    private static byte[] getSecretByte(String secret) {
        return secret.getBytes(StandardCharsets.UTF_8);
    }
}
