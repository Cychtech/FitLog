package com.example.fitlog.ui.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitlog.R;
import com.example.fitlog.database.WorkoutDatabaseHelper;
import com.example.fitlog.models.Exercise;
import com.example.fitlog.models.Workout;

import java.util.List;

public class WorkoutDetailActivity extends AppCompatActivity {

    private TextView workoutTitle;
    private LinearLayout exerciseContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        // Bind views
        workoutTitle = findViewById(R.id.textWorkoutTitle);
        exerciseContainer = findViewById(R.id.exerciseContainer);

        // Get workout ID from intent
        int workoutId = getIntent().getIntExtra("workout_id", -1);
        if (workoutId != -1) {
            loadWorkoutDetails(workoutId);
        }
    }

    private void loadWorkoutDetails(int workoutId) {
        WorkoutDatabaseHelper dbHelper = new WorkoutDatabaseHelper(this);

        // Fetch workout
        Workout workout = dbHelper.getWorkoutById(workoutId);
        if (workout == null) return;

        // Fetch and set exercises
        List<Exercise> exercises = dbHelper.getExercisesForWorkout(workoutId);
        workout.setExercises(exercises);

        // Set workout name
        workoutTitle.setText(workout.getName());

        // Render each exercise
        for (Exercise exercise : exercises) {
            View exerciseView = LayoutInflater.from(this).inflate(R.layout.item_exercise_detail, exerciseContainer, false);
            TextView name = exerciseView.findViewById(R.id.exerciseName);
            TextView equipment = exerciseView.findViewById(R.id.exerciseEquipment);
            LinearLayout setsContainer = exerciseView.findViewById(R.id.setsContainer);

            name.setText(exercise.getName());
            equipment.setText(exercise.getEquipment());

            for (int i = 1; i <= 5; i++) {
                TextView setView = new TextView(this);
                setView.setText(i + ". 5 x _ KG");
                setsContainer.addView(setView);
            }

            exerciseContainer.addView(exerciseView);
        }
    }
}
