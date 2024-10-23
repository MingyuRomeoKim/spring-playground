package com.mingyu.playground.repository;


import com.mingyu.playground.entity.ArticleHit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleHitRepository extends CrudRepository<ArticleHit, String>, ArticleHitRepositoryCustom {

    List<ArticleHit> findAllBy();
}
