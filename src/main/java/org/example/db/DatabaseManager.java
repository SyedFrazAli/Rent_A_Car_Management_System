package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:rentacar.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            // Test the connection immediately to fail fast if driver is missing
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                System.out.println("SQLite JDBC driver loaded successfully");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver: " + e.getMessage());
            System.exit(1); // Exit if driver not found
        } catch (SQLException e) {
            System.err.println("Failed to establish test connection: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createCarsTable = """
            CREATE TABLE IF NOT EXISTS cars (
                id TEXT PRIMARY KEY,
                model TEXT NOT NULL,
                type TEXT NOT NULL,
                is_available BOOLEAN DEFAULT TRUE,
                battery_capacity INTEGER,
                has_chauffeur BOOLEAN
            )""";

        String createBookingsTable = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                car_id TEXT NOT NULL,
                customer_name TEXT NOT NULL,
                booking_type TEXT NOT NULL,
                includes_insurance BOOLEAN,
                FOREIGN KEY (car_id) REFERENCES cars (id)
            )""";

        String createAdminTable = """
            CREATE TABLE IF NOT EXISTS admin (
                username TEXT PRIMARY KEY,
                password TEXT NOT NULL
            )""";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Enable foreign key constraints
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute(createCarsTable);
            stmt.execute(createBookingsTable);
            stmt.execute(createAdminTable);

            // Insert default admin if not exists
            stmt.execute("""
                INSERT OR IGNORE INTO admin (username, password) 
                VALUES ('admin', 'admin123')
                """);

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}