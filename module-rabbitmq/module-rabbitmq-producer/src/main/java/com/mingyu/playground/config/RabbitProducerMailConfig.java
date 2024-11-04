package com.mingyu.playground.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProducerMailConfig {

    @Value("${rabbitmq.playground.mail.queue.name}")
    private String queueName;

    @Value("${rabbitmq.playground.mail.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.playground.mail.routing-key.name}")
    private String routingKeyName;

    @Value("${rabbitmq.playground.mail.dead-letter-queue.name}")
    private String deadLetterQueueName;

    @Value("${rabbitmq.playground.mail.dead-letter-exchange.name}")
    private String deadLetterExchangeName;

    @Value("${rabbitmq.playground.mail.dead-letter-routing-key.name}")
    private String deadLetterRoutingKeyName;

    @Bean
    public Queue playgroundMailQueue() {
        Queue queue = new Queue(queueName, true);
        queue.addArgument("x-dead-letter-exchange", deadLetterExchangeName);
        queue.addArgument("x-dead-letter-routing-key", deadLetterRoutingKeyName);

        return queue;
    }

    @Bean
    public Queue playgroundMailDeadLetterQueue() {
        return new Queue(deadLetterQueueName, true);
    }

    @Bean
    public TopicExchange playgroundMailExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public TopicExchange playgroundMailDeadLetterExchange() {
        return new TopicExchange(deadLetterExchangeName);
    }

    @Bean
    public Binding playgroundMailBinding() {
        return BindingBuilder.bind(playgroundMailQueue()).to(playgroundMailExchange()).with(routingKeyName);
    }

    @Bean
    public Binding playgroundMailDeadLetterBinding() {
        return BindingBuilder.bind(playgroundMailDeadLetterQueue()).to(playgroundMailDeadLetterExchange()).with(deadLetterRoutingKeyName);
    }
}
