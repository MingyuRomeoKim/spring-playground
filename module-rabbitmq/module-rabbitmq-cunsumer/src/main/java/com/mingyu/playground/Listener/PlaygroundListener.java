package com.mingyu.playground.Listener;

import com.mingyu.playground.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PlaygroundListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.playground.queue.name}")
    public void receiveMessage(String message) throws AmqpRejectAndDontRequeueException{
        System.out.println("Received <" + message + ">");

        Map<String,String> contextMap = Map.of("name", message);
        String content = emailService.setContext("mail-default", contextMap);
        emailService.sendEmail("rlaalsrb0466@naver.com", "할 일 목록", content);
    }

    @RabbitListener(queues =  "${rabbitmq.playground.dead-letter-queue.name}")
    public void receiveDeadLetterMessage(String message) {
        System.out.println("Received Dead Letter <" + message + ">");
    }
}
