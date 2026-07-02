package com.example.cyber_features;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button scanButton;

    RecyclerView recyclerView;

    ProgressBar progressBar;

    List<AppModel> appList =
            new ArrayList<>();

    AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        scanButton =
                findViewById(R.id.scanButton);

        recyclerView =
                findViewById(R.id.recyclerView);

        progressBar =
                findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        adapter =
                new AppAdapter(appList);

        recyclerView.setAdapter(adapter);

        // Swipe to remove threat apps
        ItemTouchHelper helper =
                new ItemTouchHelper(
                        new ItemTouchHelper.SimpleCallback(
                                0,
                                ItemTouchHelper.LEFT |
                                        ItemTouchHelper.RIGHT) {

                            @Override
                            public boolean onMove(
                                    RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder,
                                    RecyclerView.ViewHolder target) {

                                return false;
                            }

                            @Override
                            public void onSwiped(
                                    RecyclerView.ViewHolder viewHolder,
                                    int direction) {

                                int position =
                                        viewHolder.getAdapterPosition();

                                appList.remove(position);

                                adapter.notifyItemRemoved(position);

                                Toast.makeText(
                                        MainActivity.this,
                                        "Threat Removed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

        helper.attachToRecyclerView(recyclerView);

        scanButton.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(
                    this::scanApps,
                    3000);
        });
    }

    private void scanApps() {

        appList.clear();

        PackageManager pm =
                getPackageManager();

        List<PackageInfo> packages =
                pm.getInstalledPackages(0);

        for (PackageInfo packageInfo : packages) {

            ApplicationInfo appInfo =
                    packageInfo.applicationInfo;

            String appName =
                    pm.getApplicationLabel(appInfo)
                            .toString();

            String packageName =
                    packageInfo.packageName
                            .toLowerCase();

            // Detect suspicious apps only
            if(packageName.contains("hack") ||
                    packageName.contains("spy") ||
                    packageName.contains("mod") ||
                    packageName.contains("cheat") ||
                    packageName.contains("inject") ||
                    packageName.contains("crack")) {

                appList.add(
                        new AppModel(
                                appName,
                                "THREAT DETECTED"));
            }
        }

        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);

        if(appList.isEmpty()) {

            Toast.makeText(
                    this,
                    "No Threat Apps Found",
                    Toast.LENGTH_LONG).show();
        }
    }
}