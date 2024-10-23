package com.mingyu.playground.repository;


import com.mingyu.playground.entity.ArticleHit;

import java.util.List;
import java.util.Map;

public interface ArticleHitRepositoryCustom {
    int incrementPcHitCount(String articleId, Integer hits);
    int incrementMobileHitCount(String articleId, Integer hits);
    List<String> getMembers();
    Map<String,Object> getAndDeleteAllHitCount(String articleId);
    List<ArticleHit> getAndDeleteAllArticleHits();
}
