package com.mingyu.playground.Listener;

import com.mingyu.playground.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PlaygroundMailListener implements PlaygroundListener{

    private final EmailService emailService;

    @Override
    @RabbitListener(queues = "${rabbitmq.playground.mail.queue.name}")
    public void receiveMessage(String message) throws AmqpRejectAndDontRequeueException {
        String content = emailService.setContext(message);

        emailService.sendEmail("rlaalsrb0466@naver.com", "할 일 목록", content);
    }

    @Override
    @RabbitListener(queues = "${rabbitmq.playground.mail.dead-letter-queue.name}")
    public void receiveDeadLetterMessage(String message) {
        System.out.println("Mail Received Dead Letter <" + message + ">");
    }

}
