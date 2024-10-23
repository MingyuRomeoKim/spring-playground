package com.mingyu.playground.domain.jwt.infrastructure;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String token; // 토큰
    private Object principal; // 토큰의 주체
    private Object credentials; // 토큰의 자격 증명

    /**
     * 인증된 토큰을 생성하는 생성자
     * @param authorities 권한 목록
     * @param principal 주체
     * @param credentials 자격 증명
     */
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true); // 인증 설정
    }

    /**
     * 인증되지 않은 토큰을 생성하는 생성자
     * @param token 토큰
     */
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false); // 토큰을 인증되지 않는 상태로 설정함.
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
