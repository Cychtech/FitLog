package com.example.fitlog.ui.routine;

import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitlog.database.WorkoutDatabaseHelper;
import com.example.fitlog.models.Workout;
import com.example.fitlog.ui.exercise.AddExerciseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.appbar.MaterialToolbar;
import com.example.fitlog.R;

import java.util.ArrayList;

public class AddWorkoutActivity extends AppCompatActivity {

    private TextInputEditText nameInput, notesInput;
    private MaterialToolbar toolbar;

    private final ActivityResultLauncher<Intent> addExerciseLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ArrayList<String> selectedExercises =
                            result.getData().getStringArrayListExtra("selected_exercises");

                    // TODO: store in local list or show on UI
                    Toast.makeText(this, "Added: " + selectedExercises.size() + " exercises", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        // Bind views
        nameInput = findViewById(R.id.editRoutineName);
        notesInput = findViewById(R.id.editRoutineNotes);
        MaterialButton addExercisesBtn = findViewById(R.id.btnAddExercises);
        toolbar = findViewById(R.id.toolbar);

        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Routine"); // ✅ This overrides app name

        toolbar.setNavigationOnClickListener(v -> finish());

        // Handle tick/save button on toolbar
        toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_save_workout) {
                    String name = nameInput.getText().toString().trim();
                    String notes = notesInput.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this, "Please enter a routine name", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    Workout workout = new Workout(name, notes);
                    new WorkoutDatabaseHelper(this).insertWorkout(workout);

                    Toast.makeText(this, "Workout saved!", Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }
            return false;
        });

        // Open AddExerciseActivity when button is clicked
        addExercisesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExerciseActivity.class);
            addExerciseLauncher.launch(intent);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

}


