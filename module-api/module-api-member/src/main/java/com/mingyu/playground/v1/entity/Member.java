package com.mingyu.playground.v1.entity;

import com.mingyu.playground.dto.member.request.UpdateMemberRequestDto;
import com.mingyu.playground.entity.DefaultTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor
public class Member extends DefaultTime {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(100)", nullable = false, unique = true)
    private String email;
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String password;
    @Column(columnDefinition = "varchar(100)", nullable = false, unique = true)
    private String phone;
    @Column(columnDefinition = "varchar(300)", nullable = false)
    private String address;
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    @Column(nullable = false)
    private Authority role;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isActive;

    public Member(String name, String email, String password, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = Authority.USER;
    }

    public void update(UpdateMemberRequestDto updateMemberRequestDto) {
        this.name = updateMemberRequestDto.getName();
        this.password = updateMemberRequestDto.getPassword();
        this.phone = updateMemberRequestDto.getPhone();
        this.address = updateMemberRequestDto.getAddress();
    }
}
