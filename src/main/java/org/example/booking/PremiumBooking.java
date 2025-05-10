package org.example.booking;

public class PremiumBooking extends RegularBooking {
    private boolean includesInsurance;

    public PremiumBooking(String carId, String customerName, boolean includesInsurance) {
        super(carId, customerName);
        this.includesInsurance = includesInsurance;
        this.basePrice = 80.00; // Premium daily rate
    }

    // Add this getter method
    public boolean includesInsurance() {
        return includesInsurance;
    }

    @Override
    public double getTotalPrice(int days) {
        return includesInsurance ? super.getTotalPrice(days) + 30 : super.getTotalPrice(days);
    }

    @Override
    public String getBookingDetails() {
        return "Booking for " + getCustomerName() + " (Car: " + getCarId() +
                ") - Premium" + (includesInsurance ? " [Insurance]" : "");
    }
}