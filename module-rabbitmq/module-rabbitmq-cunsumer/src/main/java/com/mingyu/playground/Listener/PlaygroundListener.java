package com.mingyu.playground.Listener;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PlaygroundListener {

    @RabbitListener(queues = "${rabbitmq.playground.queue.name}")
    public void receiveMessage(String message) throws AmqpRejectAndDontRequeueException{
        System.out.println("Received <" + message + ">");
    }

    @RabbitListener(queues =  "${rabbitmq.playground.dead-letter-queue.name}")
    public void receiveDeadLetterMessage(String message) {
        System.out.println("Received Dead Letter <" + message + ">");
    }
}
