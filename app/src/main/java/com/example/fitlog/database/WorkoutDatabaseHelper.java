package com.example.fitlog.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fitlog.models.Exercise;
import com.example.fitlog.models.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FitLog.db";
    private static final int DB_VERSION = 2;

    public WorkoutDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS workouts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, notes TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS workout_exercises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "workout_id INTEGER, " +
                "exercise_id INTEGER, " +
                "FOREIGN KEY(workout_id) REFERENCES workouts(id), " +
                "FOREIGN KEY(exercise_id) REFERENCES exercises(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS exercises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "type TEXT, " +
                "description TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS exercise_equipment (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exercise_id INTEGER, " +
                "equipment TEXT, " +
                "FOREIGN KEY(exercise_id) REFERENCES exercises(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS exercise_primary_muscles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exercise_id INTEGER, " +
                "muscle TEXT, " +
                "FOREIGN KEY(exercise_id) REFERENCES exercises(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS exercise_secondary_muscles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exercise_id INTEGER, " +
                "muscle TEXT, " +
                "FOREIGN KEY(exercise_id) REFERENCES exercises(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS workouts");
        db.execSQL("DROP TABLE IF EXISTS workout_exercises");
        db.execSQL("DROP TABLE IF EXISTS exercises");
        db.execSQL("DROP TABLE IF EXISTS exercise_equipment");
        db.execSQL("DROP TABLE IF EXISTS exercise_primary_muscles");
        db.execSQL("DROP TABLE IF EXISTS exercise_secondary_muscles");
        onCreate(db);
    }

    public void insertWorkout(Workout workout) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", workout.getName());
        values.put("notes", workout.getNotes());
        db.insert("workouts", null, values);
        db.close();
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM workouts ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Workout workout = new Workout(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                list.add(workout);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Workout getWorkoutById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("workouts", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int workoutId = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String notes = cursor.getString(cursor.getColumnIndex("notes"));

            Workout workout = new Workout(workoutId, name, notes);
            cursor.close();
            return workout;
        }

        return null;
    }

    public List<Exercise> getExercisesForWorkout(int workoutId) {
        List<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT e.id, e.name, ee.equipment " +
                "FROM workout_exercises we " +
                "JOIN exercises e ON we.exercise_id = e.id " +
                "LEFT JOIN exercise_equipment ee ON e.id = ee.exercise_id " +
                "WHERE we.workout_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(workoutId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String equipment = cursor.getString(cursor.getColumnIndexOrThrow("equipment"));

                Exercise exercise = new Exercise(id, name, equipment);
                exerciseList.add(exercise);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return exerciseList;
    }

    public long insertExercise(String name, String type, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("type", type);
        values.put("description", description);
        return db.insert("exercises", null, values);
    }

    public void insertExerciseEquipment(long exerciseId, List<String> equipmentList) {
        SQLiteDatabase db = getWritableDatabase();
        for (String equipment : equipmentList) {
            ContentValues values = new ContentValues();
            values.put("exercise_id", exerciseId);
            values.put("equipment", equipment);
            db.insert("exercise_equipment", null, values);
        }
    }

    public void insertExerciseMuscles(long exerciseId, List<String> primary, List<String> secondary) {
        SQLiteDatabase db = getWritableDatabase();

        for (String muscle : primary) {
            ContentValues values = new ContentValues();
            values.put("exercise_id", exerciseId);
            values.put("muscle", muscle);
            db.insert("exercise_primary_muscles", null, values);
        }

        for (String muscle : secondary) {
            ContentValues values = new ContentValues();
            values.put("exercise_id", exerciseId);
            values.put("muscle", muscle);
            db.insert("exercise_secondary_muscles", null, values);
        }
    }

    public List<String> getExerciseEquipment(int exerciseId) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("exercise_equipment", new String[]{"equipment"}, "exercise_id=?", new String[]{String.valueOf(exerciseId)}, null, null, null);

        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndexOrThrow("equipment")));
        }
        cursor.close();
        return list;
    }

    public List<String> getPrimaryMuscles(int exerciseId) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("exercise_primary_muscles", new String[]{"muscle"}, "exercise_id=?", new String[]{String.valueOf(exerciseId)}, null, null, null);

        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndexOrThrow("muscle")));
        }
        cursor.close();
        return list;
    }

    public List<String> getSecondaryMuscles(int exerciseId) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("exercise_secondary_muscles", new String[]{"muscle"}, "exercise_id=?", new String[]{String.valueOf(exerciseId)}, null, null, null);

        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndexOrThrow("muscle")));
        }
        cursor.close();
        return list;
    }
}
