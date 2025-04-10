package com.example.fitlog.models;
public class Exercise {
    private int id;
    private String name;
    private String equipment;
    private boolean selected;

    public Exercise(int id, String name, String equipment) {
        this.id = id;
        this.name = name;
        this.equipment = equipment;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEquipment() { return equipment; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}
