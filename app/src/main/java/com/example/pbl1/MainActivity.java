package com.example.pbl1;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button scanButton;
    LinearLayout resultContainer;
    TextView loadingText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = findViewById(R.id.scanButton);
        resultContainer = findViewById(R.id.resultContainer);
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        scanButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
            resultContainer.removeAllViews();

            new Handler().postDelayed(this::scanApps, 800);
        });
    }

    private void scanApps() {

        PackageManager packageManager = getPackageManager();

        List<ApplicationInfo> apps =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : apps) {

            try {

                PackageInfo packageInfo = packageManager.getPackageInfo(
                        app.packageName, PackageManager.GET_PERMISSIONS
                );

                String[] permissions = packageInfo.requestedPermissions;

                StringBuilder permText = new StringBuilder();
                int riskScore = 0;

                if (permissions != null) {
                    for (String permission : permissions) {

                        permText.append("• ").append(permission).append("\n");

                        if (permission.contains("READ_CONTACTS")) riskScore += 25;
                        if (permission.contains("SEND_SMS")) riskScore += 30;
                        if (permission.contains("RECORD_AUDIO")) riskScore += 20;
                        if (permission.contains("ACCESS_FINE_LOCATION")) riskScore += 20;
                        if (permission.contains("CAMERA")) riskScore += 15;
                        if (permission.contains("READ_SMS")) riskScore += 25;
                    }
                } else {
                    permText.append("No Permissions Requested");
                }

                String riskLevel;
                int riskColor;

                if (riskScore >= 70) {
                    riskLevel = "CRITICAL";
                    riskColor = Color.parseColor("#D32F2F");
                } else if (riskScore >= 40) {
                    riskLevel = "HIGH";
                    riskColor = Color.parseColor("#F57C00");
                } else if (riskScore >= 20) {
                    riskLevel = "MEDIUM";
                    riskColor = Color.parseColor("#FBC02D");
                } else {
                    riskLevel = "SAFE";
                    riskColor = Color.parseColor("#388E3C");
                }

                addAppCard(
                        app.loadLabel(packageManager).toString(),
                        permText.toString().trim(),
                        riskScore,
                        riskLevel,
                        riskColor
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
    }

    private void addAppCard(String appName, String permissions, int riskScore, String riskLevel, int riskColor) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(32, 28, 32, 28);

        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(Color.WHITE);
        cardBg.setCornerRadius(24f);
        card.setBackground(cardBg);
        card.setElevation(4f);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);

        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText(appName);
        title.setTextSize(16);
        title.setTextColor(Color.parseColor("#1A2233"));
        title.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        title.setLayoutParams(titleParams);
        topRow.addView(title);

        TextView badge = new TextView(this);
        badge.setText(riskLevel);
        badge.setTextSize(11);
        badge.setTypeface(null, Typeface.BOLD);
        badge.setTextColor(Color.WHITE);
        badge.setPadding(20, 8, 20, 8);

        GradientDrawable badgeBg = new GradientDrawable();
        badgeBg.setColor(riskColor);
        badgeBg.setCornerRadius(50f);
        badge.setBackground(badgeBg);
        topRow.addView(badge);

        card.addView(topRow);

        TextView scoreText = new TextView(this);
        scoreText.setText("Risk Score: " + riskScore + "/100");
        scoreText.setTextSize(12);
        scoreText.setTextColor(Color.parseColor("#666666"));
        LinearLayout.LayoutParams scoreParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        scoreParams.setMargins(0, 8, 0, 8);
        scoreText.setLayoutParams(scoreParams);
        card.addView(scoreText);

        TextView permText = new TextView(this);
        permText.setText(permissions);
        permText.setTextSize(12);
        permText.setTextColor(Color.parseColor("#444444"));
        card.addView(permText);

        resultContainer.addView(card);
    }
}