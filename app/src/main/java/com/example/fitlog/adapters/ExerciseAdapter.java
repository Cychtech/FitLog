package com.example.fitlog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitlog.R;
import com.example.fitlog.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private final List<Exercise> exerciseList;

    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView name, equipment;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkboxExercise);
            name = itemView.findViewById(R.id.textExerciseName);
            equipment = itemView.findViewById(R.id.textEquipment);
        }
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.name.setText(exercise.getName());
        holder.equipment.setText(exercise.getEquipment());
        holder.checkbox.setChecked(exercise.isSelected());

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                exercise.setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public List<Exercise> getSelectedExercises() {
        List<Exercise> selected = new ArrayList<>();
        for (Exercise e : exerciseList) {
            if (e.isSelected()) selected.add(e);
        }
        return selected;
    }
}

