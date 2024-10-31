package com.mingyu.playground.dto;

import lombok.Data;

@Data
public class SendMessageRequestDto {
    private String exchangeName;
    private String routingKeyName;
    private String message;
}
