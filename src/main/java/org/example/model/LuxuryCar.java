package org.example.model;

public class LuxuryCar extends Car {
    private boolean hasChauffeur;

    public LuxuryCar(String id, String model, boolean hasChauffeur) {
        super(id, model);
        this.hasChauffeur = hasChauffeur;
    }

    public boolean hasChauffeur() {
        return hasChauffeur;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Luxury, Chauffeur: " + (hasChauffeur ? "Yes" : "No");
    }
}