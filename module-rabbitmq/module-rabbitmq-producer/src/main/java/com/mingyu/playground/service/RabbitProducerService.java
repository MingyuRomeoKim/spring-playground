package com.mingyu.playground.service;

public interface RabbitProducerService {
    public void sendMessage(String message);
    public void sendMessage(String exchangeName, String routingKeyName, String message);
}
