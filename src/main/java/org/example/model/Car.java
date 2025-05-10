package org.example.model;

public class Car {
    private String id;
    private String model;
    private boolean isAvailable;

    public Car(String id, String model) {
        this.id = id;
        this.model = model;
        this.isAvailable = true;
    }
    @Override
    public String toString() {
        return "ID: " + id + ", Model: " + model;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}