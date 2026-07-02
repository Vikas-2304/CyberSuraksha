package com.example.cyber_features;
import com.example.cyber_features.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    List<AppModel> list;

    public AppAdapter(List<AppModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.app_item,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        AppModel model = list.get(position);

        holder.appName.setText(
                model.getAppName());

        holder.status.setText(
                model.getStatus());

        if (model.getStatus().contains("THREAT")) {

            holder.status.setTextColor(
                    Color.RED);

            holder.cardView.setCardBackgroundColor(
                    Color.parseColor("#330000"));
        }
        else {

            holder.status.setTextColor(
                    Color.GREEN);

            holder.cardView.setCardBackgroundColor(
                    Color.parseColor("#1E1E1E"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView appName, status;

        CardView cardView;

        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);

            appName =
                    itemView.findViewById(
                            R.id.appName);

            status =
                    itemView.findViewById(
                            R.id.appStatus);

            cardView =
                    itemView.findViewById(
                            R.id.cardView);
        }
    }
}