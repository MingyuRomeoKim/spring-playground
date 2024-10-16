package com.mingyu.playground.common.emtities;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DefaultTime {

    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(nullable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    // 엔티티가 저장되기 전 실행되는 메소드
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().withNano(0);  // 나노초 제거
        this.updatedAt = LocalDateTime.now().withNano(0);  // 나노초 제거
    }

    // 엔티티가 업데이트되기 전 실행되는 메소드
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().withNano(0);  // 나노초 제거
    }

    // 엔티티가 삭제되기 전 실행되는 메소드
    @PreRemove
    public void preRemove() {
        this.deletedAt = LocalDateTime.now().withNano(0);  // 나노초 제거
    }
}
