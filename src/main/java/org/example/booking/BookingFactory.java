package org.example.booking;

public class BookingFactory {
    public static RegularBooking createBooking(String type, String carId, String customerName) {
        return switch (type.toLowerCase()) {
            case "premium" -> new PremiumBooking(carId, customerName, true);
            default -> new RegularBooking(carId, customerName); // Default booking
        };
    }
}