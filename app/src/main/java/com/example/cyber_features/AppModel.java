package com.example.cyber_features;

public class AppModel {

    String appName;

    String status;

    public AppModel(
            String appName,
            String status) {

        this.appName = appName;

        this.status = status;
    }

    public String getAppName() {

        return appName;
    }

    public String getStatus() {

        return status;
    }
}