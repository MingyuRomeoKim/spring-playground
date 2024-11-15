package com.mingyu.playground.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Getter
@Setter
public class PlayGroundPageable {
    @Schema(hidden = true)
    private Pageable pageable;
    @Schema(description = "페이지당 레코드 수", example = "15")
    private Integer recordsPerPage;
    @Schema(description = "현재 페이지 번호", example = "1")
    private Integer currentPageNo;
    @Schema(description = "페이지 개수", example = "10")
    private Integer pageSize;
    @Schema(description = "정렬", example = "payDate,desc")
    private String sort;

    public PlayGroundPageable(Integer currentPageNo, Integer recordsPerPage, Integer pageSize, String sort) {
        this.pageSize = Objects.requireNonNullElse(pageSize, 10);
        this.recordsPerPage = Objects.requireNonNullElse(recordsPerPage, 10);
        this.currentPageNo = Objects.requireNonNullElse(currentPageNo, 1);
        this.sort = Objects.requireNonNullElse(sort, "");

        Sort sortObject = Sort.unsorted();

        if ( sort != null && !sort.isEmpty()) {
            sortObject = parseSortString(this.sort);
        }

        this.pageable = PageRequest.of(this.currentPageNo - 1, this.recordsPerPage, sortObject);
    }

    private Sort parseSortString(String sortStr) {
        // String을 ','로 구분하여 필드명과 정렬 방향을 분리
        String[] sortParams = sortStr.split(",");

        // 필드명과 정렬 방향 확인
        String sortField = sortParams[0].trim(); // 필드명
        Sort.Direction direction = Sort.Direction.ASC; // 기본값은 ASC

        if (sortParams.length > 1) {
            // 정렬 방향이 있으면 이를 사용
            direction = Sort.Direction.fromOptionalString(sortParams[1].trim()).orElse(Sort.Direction.ASC);
        }

        // Sort 객체 반환
        return Sort.by(direction, sortField);
    }
}

