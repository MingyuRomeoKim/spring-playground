package com.mingyu.playground.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class UpdateMemberRequestDto {
    private String name;
    private String password;
    private String phone;
    private String address;
}
