package com.mingyu.playground.Listener;

public interface PlaygroundListener {
    void receiveMessage(String message);
    void receiveDeadLetterMessage(String message);
}
