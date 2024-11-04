package com.mingyu.playground.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(String email, String subject, String context) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(context, true);
            javaMailSender.send(message);
        } catch (Exception exception) {
            log.error("Failed to send email to " + email, exception);
        }
    }

    @Override
    public String setContext(String templateName, Map<String,String> contextMap) {
        Context context = new Context();
        contextMap.forEach(context::setVariable);

        return templateEngine.process(templateName, context);
    }
}
