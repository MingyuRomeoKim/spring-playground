package com.mingyu.playground.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "auth-login", timeToLive = 300L)
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthLogin {
    @Id
    private String accessToken;
    private boolean isLogin;
}
