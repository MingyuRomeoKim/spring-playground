package com.mingyu.playground.v1.entity;

import com.mingyu.playground.entity.DefaultTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Builder
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
public class Token extends DefaultTime {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "varchar(500)", nullable = false)
    private String accessToken;

    @Column(columnDefinition = "varchar(500)", nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String grantType;

    public Token update(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Token update(String accessToken, String refreshToken, String grantType) {
        return Token.builder()
                .id(this.id)
                .member(this.member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(grantType)
                .build();
    }
}
