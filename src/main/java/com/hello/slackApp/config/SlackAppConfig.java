package com.hello.slackApp.config;

import com.hello.slackApp.service.ChatgptService;
import com.hello.slackApp.service.PrometheusService;
import com.hello.slackApp.service.SlackAlertService;
import com.hello.slackApp.service.SchedulerService;
import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class SlackAppConfig {

    private final Logger log = LoggerFactory.getLogger(SlackAppConfig.class);

    private final String token;
    private final String signingSecret;
    @Autowired
    private ChatgptService chatgptService;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private PrometheusService prometheusService;

    private static final String WAIT_MESSAGE = ":robot_face: :speech_balloon: 잠시만 기다려주세요. ChatGPT가 답변을 작성하고 있습니다.";

    public SlackAppConfig(Environment env) {
        this.token = env.getProperty("token");
        this.signingSecret = env.getProperty("signingSecret");
    }
    @Qualifier("bot")
    @Bean
    public App initSlackApp(){
        AppConfig appConfig = AppConfig.builder().singleTeamBotToken(token).signingSecret(signingSecret).build();
        App app = new App(appConfig);

        app.command("/bot", (req, ctx)->{
            SlashCommandPayload payload = req.getPayload();
            String userId = "<@" + payload.getUserId() + ">";
            String query = payload.getText();
            ctx.respond(r -> r.responseType("in_channel").text(":question: " + userId + "님의 질문 : " + query));
            ctx.respond(r -> r.responseType("in_channel").text(WAIT_MESSAGE));
            String gpt_resp = chatgptService.processSearch(query);
            log.info(query);

            String metric_result = prometheusService.processQuery(gpt_resp);
            ctx.respond(r -> r.responseType("in_channel").text(gpt_resp));
            ctx.respond(r -> r.responseType("in_channel").text("요청 값은 다음과 같습니다: "+ metric_result));
            return ctx.ack();
        });

        app.command("/alert", (req, ctx)->{
            SlashCommandPayload payload = req.getPayload();
            String query = payload.getText();
            ctx.respond(r -> r.responseType("in_channel").text("Alert 정상 등록 완료 되었습니다."));

            String[] alert = query.split(" ");
            if (alert[0].equals("insert")){
                schedulerService.addToMap(alert);
            }
            if (alert[0].equals("delete")){
                schedulerService.removeFromMap(alert[1]);
            }

            return ctx.ack();
        });

        return app;
    }
}
