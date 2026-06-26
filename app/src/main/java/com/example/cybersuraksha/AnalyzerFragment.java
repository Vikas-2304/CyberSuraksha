package com.example.cybersuraksha;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyzerFragment extends Fragment {
    private ListView appListView;
    private CheckBox cbHideSystem;
    private ProgressBar progressBar;
    private List<AppInfo> allApps = new ArrayList<>();
    private List<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analyzer, container, false);
        appListView = view.findViewById(R.id.app_list);
        cbHideSystem = view.findViewById(R.id.cb_hide_system);
        progressBar = view.findViewById(R.id.analyzer_progress);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, displayList);
        appListView.setAdapter(adapter);

        cbHideSystem.setOnCheckedChangeListener((buttonView, isChecked) -> filterApps());

        loadApps();

        return view;
    }

    private void loadApps() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            PackageManager pm = requireContext().getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
            allApps.clear();

            for (PackageInfo pi : packages) {
                boolean isSystem = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
                List<String> perms = pi.requestedPermissions != null ? Arrays.asList(pi.requestedPermissions) : new ArrayList<>();
                allApps.add(new AppInfo(pi.packageName, pm.getApplicationLabel(pi.applicationInfo).toString(), pm.getApplicationIcon(pi.applicationInfo), perms, isSystem));
            }

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    filterApps();
                });
            }
        }).start();
    }

    private void filterApps() {
        displayList.clear();
        boolean hideSystem = cbHideSystem.isChecked();
        for (AppInfo app : allApps) {
            if (hideSystem && app.isSystemApp) continue;
            displayList.add(app.appName + " (Risk: " + app.riskScore + ")");
        }
        adapter.notifyDataSetChanged();
    }
}