package com.mingyu.playground.domain.redis.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "viewCount", timeToLive = 300L)
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ViewCount {
    @Id
    private String userName;
    private String verifyCode;
}


