package com.mingyu.playground.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConsumerConfig {

    @Value("${rabbitmq.playground.queue.name}")
    private String queueName;

    @Value("${rabbitmq.playground.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.playground.routing-key.name}")
    private String routingKeyName;

    @Value("${rabbitmq.playground.dead-letter-queue.name}")
    private String deadLetterQueueName;

    @Value("${rabbitmq.playground.dead-letter-exchange.name}")
    private String deadLetterExchangeName;

    @Value("${rabbitmq.playground.dead-letter-routing-key.name}")
    private String deadLetterRoutingKeyName;

    @Bean
    public Queue playgroundQueue() {
        Queue queue = new Queue(queueName, true);
        queue.addArgument("x-dead-letter-exchange", deadLetterExchangeName);
        queue.addArgument("x-dead-letter-routing-key", deadLetterRoutingKeyName);

        return queue;
    }

    @Bean
    public Queue playgroundDeadLetterQueue() {
        return new Queue(deadLetterQueueName, true);
    }

    @Bean
    public TopicExchange playgroundExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public TopicExchange playgroundDeadLetterExchange() {
        return new TopicExchange(deadLetterExchangeName);
    }

    @Bean
    public Binding playgroundBinding() {
        return BindingBuilder.bind(playgroundQueue()).to(playgroundExchange()).with(routingKeyName);
    }

    @Bean
    public Binding playgroundDeadLetterBinding() {
        return BindingBuilder.bind(playgroundDeadLetterQueue()).to(playgroundDeadLetterExchange()).with(deadLetterRoutingKeyName);
    }
}
