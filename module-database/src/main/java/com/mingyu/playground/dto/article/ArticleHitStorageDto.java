package com.mingyu.playground.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
@Builder
@Schema(description = "ArticleHitStorage 사용을 위한 DTO")
public class ArticleHitStorageDto {
    @Schema(description = "기사 아이디", defaultValue = "20240124135529")
    String articleId;
    @Schema(description = "PC 조회수", defaultValue = "0")
    private int hitCount;
    @Schema(description = "모바일 조회수", defaultValue = "0")
    private int mobileHitCount;
}
