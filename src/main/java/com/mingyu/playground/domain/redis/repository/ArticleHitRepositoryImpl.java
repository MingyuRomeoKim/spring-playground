package com.mingyu.playground.domain.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleHitRepositoryImpl implements ArticleHitRepositoryCustom {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_PREFIX = "article-hit:";

    @Override
    public int incrementHitCount(String articleId, Integer hits) {
        String key = KEY_PREFIX + articleId;
        Long updatedValue = redisTemplate.opsForHash().increment(key, "hitCount", hits);

        return updatedValue.intValue();
    }

    @Override
    public int incrementMobileHitCount(String articleId, Integer hits) {
        String key = KEY_PREFIX + articleId;
        Long updatedValue = redisTemplate.opsForHash().increment(key, "mobileHitCount", hits);

        return updatedValue.intValue();
    }
}
