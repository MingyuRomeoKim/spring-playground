package com.mingyu.playground.domain.redis.repository;

public interface ArticleHitRepositoryCustom {

    int incrementHitCount(String articleId, Integer hits);
    int incrementMobileHitCount(String articleId, Integer hits);

}
