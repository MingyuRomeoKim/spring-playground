package com.mingyu.playground.domain.article.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "article_hits")
@NoArgsConstructor
public class ArticleHit {
    @Id
    @OneToOne
    @JoinColumn(name = "article_id")
    private Article article;
    @Column(columnDefinition = "bigint default 0")
    private Integer hitCount;
    @Column(columnDefinition = "bigint default 0")
    private Integer mobileHitCount;

    public void increasePcHits(Integer hits) {
        this.hitCount += hits;
    }

    public void increaseMobileHits(Integer hits) {
        this.mobileHitCount += hits;
    }
}
