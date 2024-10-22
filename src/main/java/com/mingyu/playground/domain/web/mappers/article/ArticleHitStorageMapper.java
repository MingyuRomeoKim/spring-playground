package com.mingyu.playground.domain.web.mappers.article;

import com.mingyu.playground.domain.web.v1.article.dto.ArticleHitStorageDto;
import com.mingyu.playground.domain.web.v1.article.entities.ArticleHitStorage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleHitStorageMapper {
    ArticleHitStorageMapper INSTANCE = Mappers.getMapper(ArticleHitStorageMapper.class);

    ArticleHitStorage toEntity(ArticleHitStorageDto articleHitStorageDto);

    ArticleHitStorageDto toDto(ArticleHitStorage articleHitStorage);

}
