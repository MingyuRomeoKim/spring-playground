package com.mingyu.playground.controller;

import com.mingyu.playground.dto.SendMessageRequestDto;
import com.mingyu.playground.service.RabbitProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rabbitmq/producer")
@RequiredArgsConstructor
public class RabbitProducerController {

    private final RabbitProducerService rabbitProducerService;

    @GetMapping("/test")
    public String test() {
        return "RabbitMQ Producer Test";
    }

    @PutMapping("/send")
    public String sendMessage(@RequestBody SendMessageRequestDto sendMessageRequestDto) {

        if (sendMessageRequestDto.getExchangeName() != null && sendMessageRequestDto.getRoutingKeyName() != null) {
            rabbitProducerService.sendMessage(sendMessageRequestDto.getExchangeName(), sendMessageRequestDto.getRoutingKeyName(), sendMessageRequestDto.getMessage());
            return "Message sent: " + sendMessageRequestDto.getMessage();
        } else {
            rabbitProducerService.sendMessage(sendMessageRequestDto.getMessage());
            return "Message sent: " + sendMessageRequestDto.getMessage();
        }
    }
}
