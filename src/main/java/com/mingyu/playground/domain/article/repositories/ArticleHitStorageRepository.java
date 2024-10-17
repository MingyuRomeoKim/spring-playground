package com.mingyu.playground.domain.article.repositories;

import com.mingyu.playground.domain.article.entities.ArticleHitStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleHitStorageRepository extends JpaRepository<ArticleHitStorage, String> {

    @Modifying
    @Query("UPDATE ArticleHitStorage a SET a.hitCount = a.hitCount + :hitIncrement WHERE a.articleId = :articleId")
    void incrementHitCount(@Param("articleId") String articleId, @Param("hitIncrement") int hitIncrement);

    @Modifying
    @Query("UPDATE ArticleHitStorage a SET a.mobileHitCount = a.mobileHitCount + :hitIncrement WHERE a.articleId = :articleId")
    void incrementMobileHitCount(@Param("articleId") String articleId, @Param("hitIncrement") int hitIncrement);
}
