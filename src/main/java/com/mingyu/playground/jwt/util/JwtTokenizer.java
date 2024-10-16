package com.mingyu.playground.jwt.util;


import com.mingyu.playground.common.error.PlayGroundCommonException;
import com.mingyu.playground.common.error.PlayGroundErrorCode;
import com.mingyu.playground.member.entities.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenizer {
    @Value("${jwt.access.secret}")
    private static String  accessSecret;
    @Value("${jwt.refresh.secret}")
    private static String refreshSecret;
    @Value("${jwt.access.expire}")
    public static Long accessTokenExpire;
    @Value("${jwt.refresh.expire}")
    public static Long refreshTokenExpire;

    /**
     * AccessToken 생성
     *
     * @param id
     * @param email
     * @param name
     * @param authority
     * @return AccessToken
     */
    public String createAccessToken(UUID id, String email, String name, Authority authority) {
        return createToken(id, email, name, authority, accessTokenExpire, accessSecret);
    }

    /**
     * RefreshToken 생성
     *
     * @param id
     * @param email
     * @param name
     * @param authority
     * @return RefreshToken
     */
    public String createRefreshToken(UUID id, String email, String name, Authority authority) {
        return createToken(id, email, name, authority, refreshTokenExpire, refreshSecret);
    }

    /**
     * Jwts 빌더를 사용하여 token 생성
     *
     * @param id
     * @param email
     * @param name
     * @param authority
     * @param expire
     * @param secretKey
     * @return
     */
    private String createToken(UUID id, String email, String name, Authority authority, Long expire, String secretKey) {
        byte[] secretByte = getSecretByte(secretKey);


        Claims claims = Jwts.claims().setSubject(email).build();
        claims.put("authority", authority);
        claims.put("id", id);
        claims.put("name", name);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretByte))
                .compact();
    }

    /**
     * 토큰에서 유저 아이디 얻기
     *
     * @param token 토큰
     * @return userId
     */
    public UUID getUserIdFromToken(String token) {
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token, getSecretByte(accessSecret));
        return claims.get("id") == null ? null : UUID.fromString(claims.get("id").toString());
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, getSecretByte(accessSecret));
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, getSecretByte(refreshSecret));
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
