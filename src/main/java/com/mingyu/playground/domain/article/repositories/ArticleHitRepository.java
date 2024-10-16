package com.mingyu.playground.domain.article.repositories;

import com.mingyu.playground.domain.article.entities.ArticleHit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHitRepository extends JpaRepository<ArticleHit,Long> {
}
