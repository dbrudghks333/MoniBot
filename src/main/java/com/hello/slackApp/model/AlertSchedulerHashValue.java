package com.hello.slackApp.model;

public class AlertSchedulerHashValue {
    private String queryValue;
    private String flag;
    private String count;
    private String time;

    public AlertSchedulerHashValue(String queryValue, String flag, String count, String time) {
        this.queryValue = queryValue;
        this.flag = flag;
        this.count = count;
        this.time = time;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
