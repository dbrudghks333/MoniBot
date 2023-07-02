package com.hello.slackApp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jakarta_servlet.SlackAppServlet;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.beans.factory.annotation.Qualifier;

@WebServlet("/slack/question")
public class SlackAppController extends SlackAppServlet {
    public SlackAppController(@Qualifier("bot") App app) {
        super(app);
    }
}
