package com.hello.slackApp.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ChatgptRequest {

    private String model = "text-davinci-003";
    private String prompt;
    private int temperature = 1;

    @SerializedName(value="max_tokens")
    private int maxTokens = 500;
}