package com.mingyu.playground.domain.redis.repository;

import com.mingyu.playground.domain.redis.entities.ArticleHit;
import org.springframework.data.repository.CrudRepository;

public interface ArticleHitRepository extends CrudRepository<ArticleHit, String>, ArticleHitRepositoryCustom {

}
