package com.mingyu.playground.dto.request;

import com.mingyu.playground.validations.JsonMatches;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendMessageRequestDto {
    private String exchangeName;
    private String routingKeyName;

    @NotBlank(message = "메세지 항목은 필수 입력 값입니다. JSON 형식으로 입력해주세요.")
    @JsonMatches
    private String message;
}
