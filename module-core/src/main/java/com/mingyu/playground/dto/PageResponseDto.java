package com.mingyu.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> items;
    private PaginationInfoDto paginationInfo;
}

