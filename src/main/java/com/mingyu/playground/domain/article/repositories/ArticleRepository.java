package com.mingyu.playground.domain.article.repositories;

import com.mingyu.playground.domain.article.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
