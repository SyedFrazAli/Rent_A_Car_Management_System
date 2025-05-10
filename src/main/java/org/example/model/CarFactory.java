package org.example.model;

public class CarFactory {
    public static Car createCar(String type, String id, String model) {
        return switch (type.toLowerCase()) {
            case "electric" -> new ElectricCar(id, model, 75);
            case "luxury" -> new LuxuryCar(id, model, true);
            default -> new Car(id, model);
        };
    }
}