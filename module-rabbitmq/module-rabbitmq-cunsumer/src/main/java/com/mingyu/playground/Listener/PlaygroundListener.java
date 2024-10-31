package com.mingyu.playground.Listener;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PlaygroundListener {

    @RabbitListener(queues = "${rabbitmq.playground.queue.name}")
    public void receiveMessage(String message) throws AmqpRejectAndDontRequeueException{
        System.out.println("Received <" + message + ">");

        throw new AmqpRejectAndDontRequeueException("처리실패 DLX 테스트");
    }

    @RabbitListener(queues =  "${rabbitmq.playground.dead-letter-queue.name}")
    public void receiveDeadLetterMessage(String message) {
        System.out.println("Received Dead Letter <" + message + ">");
    }
}
