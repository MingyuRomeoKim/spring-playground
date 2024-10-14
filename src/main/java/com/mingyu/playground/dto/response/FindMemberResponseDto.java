package com.mingyu.playground.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class FindMemberResponseDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;
}
