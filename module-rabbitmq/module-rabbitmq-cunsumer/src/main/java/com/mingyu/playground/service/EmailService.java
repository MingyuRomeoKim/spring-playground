package com.mingyu.playground.service;

import java.util.Map;

public interface EmailService {
    void sendEmail(String email, String subject, String content);
    String setContext(String jsonContent);
}
