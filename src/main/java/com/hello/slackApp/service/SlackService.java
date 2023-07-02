package com.hello.slackApp.service;

public interface SlackService {
    void sendChatGPTMessageToChannel(String channelId, String question);
}
