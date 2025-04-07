package com.example.fitlog.ui.exercise;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitlog.R;
import com.example.fitlog.adapters.ExerciseAdapter;
import com.example.fitlog.models.Exercise;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.appbar.MaterialToolbar;
import androidx.appcompat.widget.SearchView;
import com.example.fitlog.database.ExerciseDatabaseHelper;
import android.content.Intent;


public class AddExerciseActivity extends AppCompatActivity {

    private ExerciseAdapter adapter;
    private List<Exercise> allExercises;
    private List<Exercise> filteredList;

    private SearchView searchView;
    private ExerciseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, NewExerciseActivity.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add_custom) {
                // Navigate to custom exercise creation screen
                startActivity(new Intent(this, NewExerciseActivity.class));
                return true;
            }
            return false;
        });

        searchView = findViewById(R.id.searchView);
        dbHelper = new ExerciseDatabaseHelper(this);

        allExercises = dbHelper.getAllExercises(); // Load from DB
        filteredList = new ArrayList<>(allExercises);

        adapter = new ExerciseAdapter(filteredList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Search logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList.clear();
                for (Exercise e : allExercises) {
                    if (e.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(e);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        findViewById(R.id.btnAddSelectedExercises).setOnClickListener(v -> {
            List<Exercise> selected = adapter.getSelectedExercises();
            ArrayList<String> names = new ArrayList<>();
            for (Exercise e : selected) {
                names.add(e.getName());
            }

            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("selected_exercises", names);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, selected.size() + " added to workout", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}


