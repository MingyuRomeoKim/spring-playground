package com.mingyu.playground.dto.request;

import com.mingyu.playground.entities.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
@Schema(description = "회원 등록 요청 DTO")
public class SaveMemberRequestDto {
    @Schema(description = "이름", defaultValue = "mingyu")
    private String name;
    @Schema(description = "이메일", defaultValue = "default-email@kimmingyu.co.kr")
    private String email;
    @Schema(description = "비밀번호", defaultValue = "1q2w3e4r!!")
    private String password;
    @Schema(description = "휴대폰 번호", defaultValue = "01012345678")
    private String phone;
    @Schema(description = "회원 주소", defaultValue = "인천광역시 부평구")
    private String address;

    public Member toEntity() {
        return new Member(name, email, password, phone, address);
    }
}
