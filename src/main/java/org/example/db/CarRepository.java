package org.example.db;

import org.example.model.Car;
import org.example.model.ElectricCar;
import org.example.model.LuxuryCar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String model = rs.getString("model");
                String type = rs.getString("type");
                boolean isAvailable = rs.getBoolean("is_available");

                Car car;
                switch (type.toLowerCase()) {
                    case "electric":
                        int battery = rs.getInt("battery_capacity");
                        car = new ElectricCar(id, model, battery);
                        break;
                    case "luxury":
                        boolean hasChauffeur = rs.getBoolean("has_chauffeur");
                        car = new LuxuryCar(id, model, hasChauffeur);
                        break;
                    default:
                        car = new Car(id, model);
                }
                car.setAvailable(isAvailable);
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching cars: " + e.getMessage());
        }
        return cars;
    }

    public void addCar(Car car) {
        String sql = "INSERT INTO cars (id, model, type, is_available, battery_capacity, has_chauffeur) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getId());
            pstmt.setString(2, car.getModel());

            if (car instanceof ElectricCar) {
                pstmt.setString(3, "electric");
                pstmt.setBoolean(4, car.isAvailable());
                pstmt.setInt(5, ((ElectricCar) car).getBatteryCapacity());
                pstmt.setNull(6, Types.BOOLEAN);
            }
            else if (car instanceof LuxuryCar) {
                pstmt.setString(3, "luxury");
                pstmt.setBoolean(4, car.isAvailable());
                pstmt.setNull(5, Types.INTEGER);
                pstmt.setBoolean(6, ((LuxuryCar) car).hasChauffeur());
            }
            else {
                pstmt.setString(3, "regular");
                pstmt.setBoolean(4, car.isAvailable());
                pstmt.setNull(5, Types.INTEGER);
                pstmt.setNull(6, Types.BOOLEAN);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
        }
    }

    public void updateCarAvailability(String carId, boolean isAvailable) {
        String sql = "UPDATE cars SET is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, isAvailable);
            pstmt.setString(2, carId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating car availability: " + e.getMessage());
        }
    }
}