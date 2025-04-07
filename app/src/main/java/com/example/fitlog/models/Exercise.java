package com.example.fitlog.models;

public class Exercise {
    private int id;
    private String name;
    private String equipment;
    private boolean selected;

    // Constructor for new exercises (no ID yet)
    public Exercise(String name, String equipment) {
        this.name = name;
        this.equipment = equipment;
    }

    // Constructor for exercises loaded from DB (with ID)
    public Exercise(int id, String name, String equipment) {
        this.id = id;
        this.name = name;
        this.equipment = equipment;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEquipment() {
        return equipment;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
