package com.mingyu.playground.domain.web.v1.auth.dto.response;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponseDto {


    private String accessToken;

    private String refreshToken;

    private String email;
    private String name;
}
