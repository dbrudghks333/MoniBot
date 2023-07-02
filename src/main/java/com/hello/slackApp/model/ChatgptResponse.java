package com.hello.slackApp.model;

import lombok.Data;

import java.util.List;

@Data
public class ChatgptResponse {
    private List<ChatgptChoice> choices;
}