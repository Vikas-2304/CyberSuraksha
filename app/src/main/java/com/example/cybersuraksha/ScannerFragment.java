package com.example.cybersuraksha;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScannerFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ProgressBar progressBar;
    private TextView scanStatus;
    private ListView threatList;
    private List<String> threats = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        progressBar = view.findViewById(R.id.scan_progress);
        scanStatus = view.findViewById(R.id.scan_status);
        threatList = view.findViewById(R.id.threat_list);
        Button btnScan = view.findViewById(R.id.btn_start_scan);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, threats);
        threatList.setAdapter(adapter);

        btnScan.setOnClickListener(v -> {
            if (checkPermission()) {
                startScan();
            } else {
                requestPermission();
            }
        });

        return view;
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void startScan() {
        threats.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        scanStatus.setText("Scanning Downloads folder...");

        new Thread(() -> {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (downloadsDir.exists()) {
                scanDirectory(downloadsDir);
            }

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    scanStatus.setText("Scan complete. " + threats.size() + " threats found.");
                });
            }
        }).start();
    }

    private void scanDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file);
            } else {
                String name = file.getName();
                if (name.endsWith(".apk") || name.endsWith(".pdf") || name.endsWith(".zip")) {
                    String hash = ScannerUtils.getSHA256(file.getAbsolutePath());
                    // Dummy check: if hash starts with "00", consider it a threat for demo purposes
                    if (hash != null && hash.startsWith("00")) {
                        if (isAdded()) {
                            requireActivity().runOnUiThread(() -> {
                                threats.add("THREAT: " + file.getName());
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }
                }
            }
        }
    }
}