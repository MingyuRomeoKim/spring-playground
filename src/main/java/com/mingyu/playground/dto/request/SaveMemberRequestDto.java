package com.mingyu.playground.dto.request;

import com.mingyu.playground.entities.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class SaveMemberRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;

    public Member toEntity() {
        return new Member(name, email, password, phone, address);
    }
}
