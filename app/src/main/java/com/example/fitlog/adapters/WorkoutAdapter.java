package com.example.fitlog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitlog.R;
import com.example.fitlog.models.Workout;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final List<Workout> workouts;
    private final OnWorkoutClickListener listener;

    public WorkoutAdapter(List<Workout> workouts, OnWorkoutClickListener listener) {
        this.workouts = workouts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.title.setText(workout.getName());
        holder.subtitle.setText(workout.getNotes());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onWorkoutClick(workout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public interface OnWorkoutClickListener {
        void onWorkoutClick(Workout workout);
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textWorkoutTitle);
            subtitle = itemView.findViewById(R.id.textWorkoutSubtitle);
        }
    }
}
