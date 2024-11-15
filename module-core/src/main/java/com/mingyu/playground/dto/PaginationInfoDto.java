package com.mingyu.playground.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaginationInfoDto {
    @Schema(description = "전체 레코드 수", example = "100")
    private Long totalRecordCount;
    @Schema(description = "전체 페이지 수", example = "10")
    private Integer totalPageCount;
    @Schema(description = "페이지 당 레코드 수", example = "10")
    private Integer recordsPerPage;
    @Schema(description = "페이지 수", example = "10")
    private Integer pageSize;
    @Schema(description = "시작 페이지 번호", example = "1")
    private Integer firstPage;
    @Schema(description = "마지막 페이지 번호", example = "10")
    private Integer lastPage;
    @Schema(description = "시작 페이지 여부", example = "true")
    private boolean startPage;
    @Schema(description = "마지막 페이지 여부", example = "false")
    private boolean endPage;
    @Schema(description = "현재 페이지 번호", example = "1")
    private Integer currentPageNo;
    @Schema(description = "빈 페이지 여부", example = "false")
    private boolean empty;
}
