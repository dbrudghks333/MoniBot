package com.hello.slackApp.service;

import com.google.gson.Gson;
import com.hello.slackApp.model.ChatgptRequest;
import com.hello.slackApp.model.ChatgptResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatgptService {

    @Value("${OPEN_AI_URL}")
    private String OPEN_AI_URL;

    @Value("${OPEN_AI_KEY}")
    private String OPEN_AI_KEY;

    public String processSearch(String query) {

        ChatgptRequest chatgptRequest = new ChatgptRequest();
        List<Map> requestMessages = new ArrayList<>();
        HashMap<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", query);
        requestMessages.add(message);
        chatgptRequest.setMessages(requestMessages);


        String url = OPEN_AI_URL;

        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + OPEN_AI_KEY);

        Gson gson = new Gson();

        String body = gson.toJson(chatgptRequest);

        log.info("body: " + body);

        try {
            final StringEntity entity = new StringEntity(body, "UTF-8");
            log.info("entity: " + entity);
            post.setEntity(entity);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10*1000) // 연결 타임아웃을 5000ms로 설정
                    .setSocketTimeout(10*1000)  // 소켓 타임아웃을 5000ms로 설정
                    .build();

            try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                String responseBody = EntityUtils.toString(response.getEntity());

                log.info("responseBody: " + responseBody);

                ChatgptResponse chatGPTResponse = gson.fromJson(responseBody, ChatgptResponse.class);

                return chatGPTResponse.getChoices().get(0).getMessage().getContent();
            } catch (Exception e) {
                return "failed";
            }
        }
        catch (Exception e) {
            return "failed";
        }

    }
}