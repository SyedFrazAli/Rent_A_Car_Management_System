package org.example.model;

public class ElectricCar extends Car {
    private int batteryCapacity; // in kWh

    public ElectricCar(String id, String model, int batteryCapacity) {
        super(id, model);
        this.batteryCapacity = batteryCapacity;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Electric, Battery: " + batteryCapacity + "kWh";
    }
}