package com.mingyu.playground.domain.web.v1.article.entities;

import com.mingyu.playground.common.emtities.DefaultTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Entity
@Table(name = "article_hit_storages")
@NoArgsConstructor
public class ArticleHitStorage extends DefaultTime {
    @Id
    String articleId;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int hitCount;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int mobileHitCount;

    @Builder
    public ArticleHitStorage(String articleId, int hitCount, int mobileHitCount) {
        this.articleId = articleId;
        this.hitCount = hitCount;
        this.mobileHitCount = mobileHitCount;
    }

}