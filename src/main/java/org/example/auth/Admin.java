package org.example.auth;

import org.example.db.DatabaseManager;
import java.sql.*;

public class Admin {
    private static Admin instance;

    private Admin() {}

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    public boolean login(String inputUsername, String inputPassword) {
        String sql = "SELECT password FROM admin WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(inputPassword);
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return false;
    }
}