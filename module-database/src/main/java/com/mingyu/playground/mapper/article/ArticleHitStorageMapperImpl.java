package com.mingyu.playground.mapper.article;



import com.mingyu.playground.dto.article.ArticleHitStorageDto;
import com.mingyu.playground.entity.article.ArticleHitStorage;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-08-17T17:00:00+0900",
        comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23 Intellij IDEA (Community Edition)"
)
public class ArticleHitStorageMapperImpl implements ArticleHitStorageMapper {

    @Override
    public ArticleHitStorage toEntity(ArticleHitStorageDto articleHitStorageDto) {
        if (articleHitStorageDto == null) {
            return null;
        }

        return ArticleHitStorage.builder()
                .articleId(articleHitStorageDto.getArticleId())
                .hitCount(articleHitStorageDto.getHitCount())
                .mobileHitCount(articleHitStorageDto.getMobileHitCount())
                .build();
    }

    @Override
    public ArticleHitStorageDto toDto(ArticleHitStorage articleHitStorage) {
        if (articleHitStorage == null) {
            return null;
        }

        ArticleHitStorageDto.ArticleHitStorageDtoBuilder articleHitStorageDto = ArticleHitStorageDto.builder();
        articleHitStorageDto.articleId(articleHitStorage.getArticleId());
        articleHitStorageDto.hitCount(articleHitStorage.getHitCount());
        articleHitStorageDto.mobileHitCount(articleHitStorage.getMobileHitCount());

        return articleHitStorageDto.build();
    }
}
