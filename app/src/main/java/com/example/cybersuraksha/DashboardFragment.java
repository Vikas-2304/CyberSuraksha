package com.example.cybersuraksha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class DashboardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        view.findViewById(R.id.btn_scan_now).setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_scanner));
        
        view.findViewById(R.id.btn_app_analyzer).setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_analyzer));
        
        view.findViewById(R.id.btn_web_check).setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_web));

        return view;
    }
}