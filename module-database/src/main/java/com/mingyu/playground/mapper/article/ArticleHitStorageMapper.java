package com.mingyu.playground.mapper.article;


import com.mingyu.playground.dto.article.ArticleHitStorageDto;
import com.mingyu.playground.entity.article.ArticleHitStorage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleHitStorageMapper {
    ArticleHitStorageMapper INSTANCE = Mappers.getMapper(ArticleHitStorageMapper.class);

    ArticleHitStorage toEntity(ArticleHitStorageDto articleHitStorageDto);

    ArticleHitStorageDto toDto(ArticleHitStorage articleHitStorage);

}
