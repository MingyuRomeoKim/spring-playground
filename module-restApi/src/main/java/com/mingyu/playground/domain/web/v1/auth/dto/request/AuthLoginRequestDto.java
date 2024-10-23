package com.mingyu.playground.domain.web.v1.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthLoginRequestDto {

    @Schema(description = "이메일", defaultValue = "kimmingyu@kimmingyu.co.kr")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    @Schema(description = "비밀번호", defaultValue = "1q2w3e4r!!")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$", message = "비밀번호는 8~20자의 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

}
