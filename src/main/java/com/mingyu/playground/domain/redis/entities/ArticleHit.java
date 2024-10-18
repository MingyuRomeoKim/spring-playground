package com.mingyu.playground.domain.redis.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Setter
@Getter
@RedisHash(value = "article-hit", timeToLive = 300L)
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleHit {
    @Id
    private String articleId;
    private int hitCount;
    private int mobileHitCount;
}
