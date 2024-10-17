package com.mingyu.playground.domain.redis.repository;

import com.mingyu.playground.domain.redis.entities.ArticleHit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleHitRepository extends CrudRepository<ArticleHit, String>, ArticleHitRepositoryCustom {
    List<ArticleHit> findAllBy();
}
