// EquipmentSelectionActivity.java
package com.example.fitlog.ui.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitlog.R;

import java.util.ArrayList;

public class EquipmentSelectionActivity extends AppCompatActivity {

    private ListView listView;
    private final String[] equipmentList = {
            "Dumbbell", "Barbell", "Machine", "Bodyweight", "Kettlebell", "Cable"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_selection);

        listView = findViewById(R.id.equipmentListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice,
                equipmentList);
        listView.setAdapter(adapter);

        findViewById(R.id.btnSaveSelection).setOnClickListener(v -> {
            ArrayList<String> selected = new ArrayList<>();
            for (int i = 0; i < listView.getCount(); i++) {
                if (listView.isItemChecked(i)) {
                    selected.add(equipmentList[i]);
                }
            }

            if (selected.isEmpty()) {
                Toast.makeText(this, "Please select at least one equipment", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent result = new Intent();
            result.putStringArrayListExtra("selected_equipment", selected);
            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
}
