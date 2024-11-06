package com.mingyu.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Article", description = "Article 관련 Controller 입니다.")
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Operation(summary = "테스트", description = "테스트테스트.")
    @GetMapping("test")
    public String test() {
        return "test";
    }
}
