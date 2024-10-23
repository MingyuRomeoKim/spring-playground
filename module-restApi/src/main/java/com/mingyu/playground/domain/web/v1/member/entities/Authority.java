package com.mingyu.playground.domain.web.v1.member.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    USER,
    ADMIN
    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
