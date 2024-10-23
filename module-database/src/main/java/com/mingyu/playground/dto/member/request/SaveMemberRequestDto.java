package com.mingyu.playground.dto.member.request;

import com.mingyu.playground.domain.web.v1.member.entities.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 등록 요청 DTO")
public class SaveMemberRequestDto implements Serializable {
    @Schema(description = "이름", defaultValue = "mingyu")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(description = "이메일", defaultValue = "default-email@kimmingyu.co.kr")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    private String email;

    @Schema(description = "비밀번호", defaultValue = "1q2w3e4r!!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$", message = "비밀번호는 8~20자의 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @Schema(description = "휴대폰 번호", defaultValue = "01012345678")
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다. 01012345678 형식으로 입력해주세요.")
    private String phone;

    @Schema(description = "회원 주소", defaultValue = "인천광역시 부평구")
    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    public Member toEntity() {
        return new Member(name, email, password, phone, address);
    }
}
