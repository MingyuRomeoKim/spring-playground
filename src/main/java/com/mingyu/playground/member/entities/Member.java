package com.mingyu.playground.member.entities;

import com.mingyu.playground.member.dto.request.UpdateMemberRequestDto;
import com.mingyu.playground.common.emtities.DefaultTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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
    @ColumnDefault("'USER'")
    @Column(columnDefinition = "varchar(10)", nullable = false)
    private String role;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isActive;

    public Member(String name, String email, String password, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = "USER";
    }

    public void update(UpdateMemberRequestDto updateMemberRequestDto) {
        this.name = updateMemberRequestDto.getName();
        this.password = updateMemberRequestDto.getPassword();
        this.phone = updateMemberRequestDto.getPhone();
        this.address = updateMemberRequestDto.getAddress();
    }
}
