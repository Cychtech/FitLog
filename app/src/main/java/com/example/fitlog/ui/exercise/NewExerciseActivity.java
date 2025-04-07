package com.example.fitlog.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitlog.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class NewExerciseActivity extends AppCompatActivity {

    private EditText nameInput, descriptionInput;
    private Spinner typeSpinner;
    private Button selectEquipmentButton;

    private static final int REQUEST_EQUIPMENT = 1;
    private ArrayList<String> selectedEquipment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Init views
        nameInput = findViewById(R.id.inputExerciseName);
        descriptionInput = findViewById(R.id.inputExerciseDescription);
        typeSpinner = findViewById(R.id.spinnerExerciseType);
        //selectEquipmentButton = findViewById(R.id.btnSelectEquipment);

        // Spinner options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.exercise_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Menu action
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save_exercise) {
                saveExercise();
                return true;
            }
            return false;
        });

        // Equipment selection
        selectEquipmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EquipmentSelectionActivity.class);
            startActivityForResult(intent, REQUEST_EQUIPMENT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EQUIPMENT && resultCode == RESULT_OK && data != null) {
            selectedEquipment = data.getStringArrayListExtra("selected_equipment");
            Toast.makeText(this, "Selected: " + selectedEquipment.size() + " equipment(s)", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveExercise() {
        String name = nameInput.getText().toString().trim();
        String type = typeSpinner.getSelectedItem().toString();
        String description = descriptionInput.getText().toString().trim();

        if (name.isEmpty()) {
            nameInput.setError("Required");
            return;
        }

        // TODO: Insert into DB using WorkoutDatabaseHelper (exercise + selectedEquipment)

        Toast.makeText(this, "Exercise saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
