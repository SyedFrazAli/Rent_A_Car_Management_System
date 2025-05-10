package org.example.auth;

public class Admin {
    // Singleton instance
    private static Admin instance;
    private String username = "admin";
    private String password = "admin123"; // In real apps, never store passwords like this!

    private Admin() {} // Private constructor

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    public boolean login(String inputUsername, String inputPassword) {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }
}