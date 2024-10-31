package com.mingyu.playground.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitProducerServiceImpl implements RabbitProducerService {

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.playground.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.playground.routing-key.name}")
    private String routingKeyName;

    @Override
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyName, message);
    }

    @Override
    public void sendMessage(String exchangeName, String routingKeyName, String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyName, message);
    }
}
