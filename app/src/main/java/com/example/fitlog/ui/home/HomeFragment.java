package com.example.fitlog.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitlog.adapters.WorkoutAdapter;
import com.example.fitlog.database.WorkoutDatabaseHelper;
import com.example.fitlog.databinding.FragmentHomeBinding;
import com.example.fitlog.models.Workout;
import com.example.fitlog.ui.routine.AddWorkoutActivity;
import com.example.fitlog.ui.routine.WorkoutDetailActivity;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private WorkoutAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate layout with ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadWorkouts();

        // Handle Add Routine button click
        binding.floatingActionButtonAddRoutine.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddWorkoutActivity.class);
            startActivity(intent);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            loadWorkouts(); // Reload from DB
            binding.swipeRefreshLayout.setRefreshing(false); // Hide spinner
        });

        return root;
    }

    private void loadWorkouts() {
        WorkoutDatabaseHelper dbHelper = new WorkoutDatabaseHelper(getContext());
        List<Workout> workouts = dbHelper.getAllWorkouts();
        adapter = new WorkoutAdapter(workouts);
        binding.recyclerView.setAdapter(adapter);
        adapter = new WorkoutAdapter(workouts, workout -> {
            Intent intent = new Intent(getContext(), WorkoutDetailActivity.class);
            intent.putExtra("workout_id", workout.getId()); // pass ID
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadWorkouts();  // Refresh list every time fragment is visible
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
