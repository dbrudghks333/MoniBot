package com.hello.slackApp.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatgptRequest {

    private String model = "gpt-3.5-turbo";
    private List<Map> messages;
    private double temperature = 0.7;

    @SerializedName(value="max_tokens")
    private int maxTokens = 500;
}