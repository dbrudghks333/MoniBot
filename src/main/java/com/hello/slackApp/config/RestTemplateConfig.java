package com.hello.slackApp.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(120)) // 서버와 연결을 시도할 때 타임아웃 설정, 연결 시도 후 해당시간안에 응답 못받으면 SocketTimeoutException 발생
                .setReadTimeout(Duration.ofSeconds(120)) // 읽기연산에 대한 타임아웃 설정, 데이터를 읽는 도중 해당 시간 안에 읽지 못하면 SocketTimeoutException 발생
                .build();
    }
}