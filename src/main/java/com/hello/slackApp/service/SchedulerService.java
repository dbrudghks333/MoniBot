package com.hello.slackApp.service;

import com.hello.slackApp.model.AlertSchedulerHashValue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class SchedulerService {

    private Map<String, AlertSchedulerHashValue> map = new HashMap<>();
    private static final int TMP_RESULT = 21;
    private static final int TMP_CNT = 5;

    @Autowired
    private SlackAlertService slackAlertService;

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void incrementValuesAndLog() {
        for(String key: map.keySet()) {
            AlertSchedulerHashValue hashValue = map.get(key);
            checkAndUpdateHashValue(hashValue, key);
        }
    }

    private void checkAndUpdateHashValue(AlertSchedulerHashValue hashValue, String key) {
        String flag = hashValue.getFlag();
        int queryValue = Integer.parseInt(hashValue.getQueryValue());
        int count = Integer.parseInt(hashValue.getCount());
        int time = Integer.parseInt(hashValue.getTime());

        if ((flag.equals("up") && queryValue <= TMP_RESULT) || (flag.equals("down") && queryValue >= TMP_RESULT)) {
            count++;
            if (count >= time / TMP_CNT){
                sendAlert(key, queryValue, flag.equals("up") ? "이상" : "이하");
            }
        } else {
            count = 0;
        }
        hashValue.setCount(Integer.toString(count));
        map.put(key, hashValue);
    }

    private void sendAlert(String key, int queryValue, String condition) {
        String[] alertQuery = {key, Integer.toString(queryValue), condition, Integer.toString(TMP_RESULT)};
        slackAlertService.sendSlackNotification(alertQuery);
    }

    // Map에 값 추가
    public void addToMap(String[] alert) {
        AlertSchedulerHashValue hashValue = new AlertSchedulerHashValue(alert[2], alert[3], "0", alert[4]);
        map.put(alert[1], hashValue);
    }
    // Map에서 값 제거
    public void removeFromMap(String key) {
        map.remove(key);
    }

}
