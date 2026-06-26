package com.example.cybersuraksha;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IdentityToolsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identity_tools, container, false);
        
        EditText etEmail = view.findViewById(R.id.et_email);
        Button btnBreach = view.findViewById(R.id.btn_check_breach);
        TextView tvBreachResult = view.findViewById(R.id.tv_breach_result);
        
        EditText etPassword = view.findViewById(R.id.et_password);
        TextView tvPasswordStrength = view.findViewById(R.id.tv_password_strength);

        btnBreach.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            if (email.isEmpty()) return;
            if (email.contains("leak")) {
                tvBreachResult.setText("Found in 3 breaches! Change your passwords.");
                tvBreachResult.setTextColor(Color.parseColor("#FF5252"));
            } else {
                tvBreachResult.setText("No known breaches found.");
                tvBreachResult.setTextColor(Color.parseColor("#00E676"));
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString(), tvPasswordStrength);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void updatePasswordStrength(String password, TextView resultView) {
        if (password.isEmpty()) {
            resultView.setText("");
            return;
        }
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[^a-zA-Z0-9].*")) score++;

        switch (score) {
            case 0:
            case 1:
                resultView.setText("Strength: Weak 🔴");
                resultView.setTextColor(Color.parseColor("#FF5252"));
                break;
            case 2:
                resultView.setText("Strength: Fair 🟠");
                resultView.setTextColor(Color.parseColor("#FFAB40"));
                break;
            case 3:
                resultView.setText("Strength: Good 🟡");
                resultView.setTextColor(Color.parseColor("#FFD740"));
                break;
            case 4:
                resultView.setText("Strength: Strong 🟢");
                resultView.setTextColor(Color.parseColor("#00E676"));
                break;
        }
    }
}