package com.hello.slackApp.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class SlackAlertService {

    public void sendSlackNotification(String[] alertQuery) {
        RestTemplate restTemplate = new RestTemplate();
        String webhookUrl = "https://hooks.slack.com/services/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";
        Map<String, String> payload = new HashMap<>();
        payload.put("text", "Metric : " + alertQuery[0] + ", Value : " + alertQuery[1] + " /// 현재 값 " + alertQuery[3] + "이 설정 값 " + alertQuery[1] +alertQuery[2] + " 입니다");
        restTemplate.postForEntity(webhookUrl, payload, String.class);
    }
}
