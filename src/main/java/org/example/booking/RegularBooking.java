package org.example.booking;

public class RegularBooking {
    protected String carId;
    protected String customerName;
    protected double basePrice;

    public RegularBooking(String carId, String customerName) {
        this.carId = carId;
        this.customerName = customerName;
        this.basePrice = 50.00; // Default daily rate
    }

    public double getTotalPrice(int days) {
        return basePrice * days;
    }

    public String getBookingDetails() {
        return "Booking for " + customerName + " (Car: " + carId + ") - Regular";
    }

    public String getCarId() {
        return carId;
    }

    public String getCustomerName() {
        return customerName;
    }
}