package com.example.cybersuraksha;

import android.graphics.drawable.Drawable;
import java.util.List;

public class AppInfo {
    public String packageName;
    public String appName;
    public Drawable icon;
    public List<String> permissions;
    public int riskScore;
    public boolean isSystemApp;

    public AppInfo(String packageName, String appName, Drawable icon, List<String> permissions, boolean isSystemApp) {
        this.packageName = packageName;
        this.appName = appName;
        this.icon = icon;
        this.permissions = permissions;
        this.isSystemApp = isSystemApp;
        this.riskScore = calculateRiskScore();
    }

    private int calculateRiskScore() {
        int score = 0;
        if (permissions == null) return 0;
        for (String p : permissions) {
            if (p.contains("SMS")) score += 30;
            if (p.contains("CONTACTS")) score += 20;
            if (p.contains("LOCATION")) score += 15;
            if (p.contains("CAMERA")) score += 15;
            if (p.contains("RECORD_AUDIO")) score += 15;
        }
        return Math.min(score, 100);
    }
}