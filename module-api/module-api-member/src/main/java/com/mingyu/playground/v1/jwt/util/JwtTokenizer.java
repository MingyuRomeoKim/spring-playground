package com.mingyu.playground.v1.jwt.util;

import com.mingyu.playground.entity.member.Authority;
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
import java.util.Date;
import java.util.UUID;

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


        Claims claims = Jwts.claims().setSubject(email)
                .add("authority", authority)
                .add("id", id)
                .add("name", name)
                .build();

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
    public String getUserIdFromToken(String token) {
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token, getSecretByte(accessSecret));
        return claims.get("id") == null ? null : claims.get("id").toString();
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
