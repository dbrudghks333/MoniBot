package com.hello.slackApp.service;

import java.io.UnsupportedEncodingException;
import com.hello.slackApp.model.AlertSchedulerHashValue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hello.slackApp.service.PrometheusService;

import java.util.HashMap;
import java.util.Map;

@Service
public class SchedulerService {

    private Map<String, AlertSchedulerHashValue> map = new HashMap<>();
    private static final int TMP_RESULT = 21;
    private static final int TMP_CNT = 5;

    @Autowired
    private SlackAlertService slackAlertService;

    @Autowired
    private PrometheusService prometheusService;

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
        String metricResult = "";
    
        try {
            metricResult = prometheusService.processQuery(key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    
        double metricResultDouble = 0;
        try {
            metricResultDouble = Double.parseDouble(metricResult);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    
        if ((flag.equals("up") && queryValue <= metricResultDouble) || (flag.equals("down") && queryValue >= metricResultDouble)) {
            count++;
            if (count >= time / TMP_CNT){
                sendAlert(key, queryValue, flag.equals("up") ? "이상" : "이하",metricResultDouble);
            }
        } else {
            count = 0;
        }

        hashValue.setCount(Integer.toString(count));
        map.put(key, hashValue);
    }
    
    private void sendAlert(String key, int queryValue, String condition, double metricResultValue) {
        String[] alertQuery = {key, Integer.toString(queryValue), condition, Double.toString(metricResultValue)};
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
