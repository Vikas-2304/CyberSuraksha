package com.example.cybersuraksha;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebProtectionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_protection, container, false);
        EditText etUrl = view.findViewById(R.id.et_url);
        Button btnCheck = view.findViewById(R.id.btn_check_url);
        TextView tvResult = view.findViewById(R.id.tv_url_result);

        btnCheck.setOnClickListener(v -> {
            String url = etUrl.getText().toString();
            if (url.isEmpty()) return;

            if (url.contains("bit.ly") || url.contains("free-prize") || url.contains("malware")) {
                tvResult.setText("Danger (🔴)");
                tvResult.setTextColor(Color.parseColor("#FF5252"));
            } else {
                tvResult.setText("Safe (🟢)");
                tvResult.setTextColor(Color.parseColor("#00E676"));
            }
        });

        return view;
    }
}