package org.example.db;

import org.example.booking.PremiumBooking;
import org.example.booking.RegularBooking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    public List<RegularBooking> getAllBookings() {
        List<RegularBooking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String carId = rs.getString("car_id");
                String customerName = rs.getString("customer_name");
                String bookingType = rs.getString("booking_type");
                boolean includesInsurance = rs.getBoolean("includes_insurance");

                if ("premium".equalsIgnoreCase(bookingType)) {
                    bookings.add(new PremiumBooking(carId, customerName, includesInsurance));
                } else {
                    bookings.add(new RegularBooking(carId, customerName));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
        return bookings;
    }

    public void addBooking(RegularBooking booking) {
        String sql = "INSERT INTO bookings (car_id, customer_name, booking_type, includes_insurance) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, booking.getCarId());
            pstmt.setString(2, booking.getCustomerName());

            if (booking instanceof PremiumBooking premiumBooking) {
                pstmt.setString(3, "premium");
                pstmt.setBoolean(4, premiumBooking.includesInsurance());
            } else {
                pstmt.setString(3, "regular");
                pstmt.setBoolean(4, false); // or pstmt.setNull(4, Types.BOOLEAN)
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
    }

    public void deleteBooking(String carId) {
        String sql = "DELETE FROM bookings WHERE car_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
        }
    }
}