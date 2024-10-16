package com.mingyu.playground.domain.article.entities;

import com.mingyu.playground.common.emtities.DefaultTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "articles")
@NoArgsConstructor
public class Article extends DefaultTime {
    @Id
    Long id;
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String title;
    @Column(columnDefinition = "varchar(200)", nullable = false)
    private String description;
    @Column(columnDefinition = "text", nullable = false)
    private String content;
}
