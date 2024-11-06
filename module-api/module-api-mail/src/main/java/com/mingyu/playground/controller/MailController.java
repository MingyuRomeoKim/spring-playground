package com.mingyu.playground.controller;

import com.mingyu.playground.dto.request.SendMessageRequestDto;
import com.mingyu.playground.service.RabbitProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/mail")
@Tag(name = "Mail", description = "Mail 관련 Controller 입니다.")
@RequiredArgsConstructor
public class MailController {

    private final RabbitProducerService rabbitProducerService;

    @Operation(summary = "전송", description = """
            메일을 전송합니다. \n
            exchangeName, routingKeyName을 지정하면 해당 exchange로 메시지를 전송합니다. \n
            지정하지 않으면 default exchange로 전송합니다. \n
            message는 Json 형태의 String 입니다. \n
            templateName을 지정하면 해당 템플릿을 사용하여 메일을 전송합니다. \n
            templateName은 필수 값이며 해당 템플릿이 존재하지 않으면 에러가 발생합니다. \n
            현재는 templateName = "mail-default"만 사용 가능합니다. \n
            
            "message": "{\\"name\\": \\"김민규\\", \\"templateName\\" : \\"mail-default\\" }"
            """)
    @PutMapping("/send")
    public String sendMessage(@Valid @RequestBody SendMessageRequestDto sendMessageRequestDto) {

        if (sendMessageRequestDto.getExchangeName() != null && sendMessageRequestDto.getRoutingKeyName() != null) {
            rabbitProducerService.sendMessage(sendMessageRequestDto.getExchangeName(), sendMessageRequestDto.getRoutingKeyName(), sendMessageRequestDto.getMessage());
            return "Message sent: " + sendMessageRequestDto.getMessage();
        } else {
            rabbitProducerService.sendMessage(sendMessageRequestDto.getMessage());
            return "Message sent: " + sendMessageRequestDto.getMessage();
        }
    }
}
