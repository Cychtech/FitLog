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

        // ViewBinding setup
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // RecyclerView setup
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WorkoutAdapter(new java.util.ArrayList<>(), workout -> {
            Intent intent = new Intent(getContext(), WorkoutDetailActivity.class);
            intent.putExtra("workout_id", workout.getId());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

        // FAB Add button
        binding.floatingActionButtonAddRoutine.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddWorkoutActivity.class);
            startActivity(intent);
        });

        // Pull-to-refresh
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            loadWorkouts();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        return root;
    }

    private void loadWorkouts() {
        WorkoutDatabaseHelper dbHelper = new WorkoutDatabaseHelper(getContext());
        List<Workout> workouts = dbHelper.getAllWorkouts();
        adapter = new WorkoutAdapter(workouts, workout -> {
            Intent intent = new Intent(getContext(), WorkoutDetailActivity.class);
            intent.putExtra("workout_id", workout.getId());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWorkouts();  // Always refresh when coming back
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
