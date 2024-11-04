package com.mingyu.playground.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingyu.playground.errors.PlayGroundCommonException;
import com.mingyu.playground.errors.PlayGroundErrorCode;
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
    private final ObjectMapper objectMapper;

    private static final String TEMPLATE_NAME_KEY = "templateName";

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
    public String setContext(String jsonContent) {
        System.out.println("Mail Received <" + jsonContent + ">");

        Map<String, Object> jsonObject = jsonParser(jsonContent);
        String templateName = jsonObject.get(TEMPLATE_NAME_KEY).toString();
        jsonObject.remove(TEMPLATE_NAME_KEY);

        Context context = new Context();
        jsonObject.forEach(context::setVariable);

        return templateEngine.process(templateName, context);
    }


    public Map<String, Object> jsonParser(String jsonContent) {
        Map<String, Object> map = null;

        try {
            map = objectMapper.readValue(
                    jsonContent, new TypeReference<>() {

                    }
            );

            if (map.isEmpty()) {
                throw new PlayGroundCommonException(PlayGroundErrorCode.JSON_EMPTY.getCode(), PlayGroundErrorCode.JSON_EMPTY.getMessage());
            }

            if (map.get(TEMPLATE_NAME_KEY) == null) {
                throw new PlayGroundCommonException(PlayGroundErrorCode.EMAIL_TEMPLATE_NOT_FOUND.getCode(), PlayGroundErrorCode.EMAIL_TEMPLATE_NOT_FOUND.getMessage());
            }

        } catch (JsonMappingException e) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.JSON_MAPPING_ERROR.getCode(), PlayGroundErrorCode.JSON_MAPPING_ERROR.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.JSON_PROCESSING_ERROR.getCode(), PlayGroundErrorCode.JSON_PROCESSING_ERROR.getMessage(), e);
        }

        return map;
    }


}
