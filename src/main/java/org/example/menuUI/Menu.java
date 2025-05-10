package org.example.menuUI;

import org.example.Main;
import org.example.booking.BookingFactory;
import org.example.booking.PremiumBooking;
import org.example.booking.RegularBooking;
import org.example.model.Car;
import org.example.model.CarFactory;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);
    private List<Car> cars;
    private List<RegularBooking> bookings;

    public Menu() {
        this.cars = new ArrayList<>();
        this.bookings = new ArrayList<>();

        // (for testing)
        cars.add(new Car("C001", "Toyota Corolla"));
        cars.add(new Car("C002", "Honda Civic"));
        cars.add(new Car("C003", "Suzuki Mehran"));
        cars.add(new Car("C004", "Suzuki Bolan"));
        cars.add(new Car("C005", "Volkswagen GTI"));
        cars.add(new Car("C006", "Mercedes-Benz"));
        cars.add(new Car("C007", "Tesla Model 3"));
        cars.add(new Car("C008", "BMW X5"));
        cars.add(new Car("C009", "Ford Fusion"));
        cars.add(new Car("C010", "Audi TT"));
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== Rent-a-Car Menu ===");
            System.out.println("1. Book a Car");
            System.out.println("2. View Bookings");
            System.out.println("3. Add a Car");
            System.out.println("4. View Cars");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Logout");
            System.out.print("Choose an option (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> bookCar();
                case 2 -> viewBookings();
                case 3 -> addCar();
                case 4 -> viewCars();
                case 5 -> cancelBooking();
                case 6 -> {
                    System.out.println("Logging out...");
                    Main.main(null);
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    private void bookCar() {
        System.out.println("\nAvailable Cars:");
        viewCars();

        System.out.print("Enter Car ID to book: ");
        String carId = scanner.nextLine();

        Car selectedCar = findCarById(carId);
        if (selectedCar == null) {
            System.out.println("Error: Car not found!");
            return;
        }

        if (!selectedCar.isAvailable()) {
            System.out.println("Error: Car already booked!");
            return;
        }

        System.out.print("Enter Your Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Booking Type (regular/premium): ");
        String bookingType = scanner.nextLine();

        // Use Factory to create booking
        RegularBooking booking = BookingFactory.createBooking(bookingType, carId, customerName);
        bookings.add(booking);
        selectedCar.setAvailable(false);
        if (booking instanceof PremiumBooking) {
            System.out.print("Enter rental days: ");
            int days = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.printf("Total price: $%.2f%n", booking.getTotalPrice(days));
        }

        System.out.println("Booking created: " + booking.getBookingDetails());
    }

    private void cancelBooking() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings to cancel!");
            return;
        }

        System.out.println("\nCurrent Bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            RegularBooking b = bookings.get(i);
            System.out.println((i+1) + ". " + b.getBookingDetails()); // Using the detailed view
        }

        System.out.print("\nEnter booking number to cancel (1-" + bookings.size() + "): ");
        int bookingNumber;
        try {
            bookingNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // Clear the invalid input
            return;
        }

        if (bookingNumber < 1 || bookingNumber > bookings.size()) {
            System.out.println("Invalid booking number!");
            return;
        }

        RegularBooking toCancel = bookings.get(bookingNumber - 1);

        // Show detailed confirmation
        System.out.println("\nYou're about to cancel this booking:");
        System.out.println(toCancel.getBookingDetails());
        if (toCancel instanceof PremiumBooking) {
            System.out.println("NOTE: This is a premium booking!");
        }

        System.out.print("\nAre you sure you want to cancel? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("Cancellation aborted.");
            return;
        }

        // Perform cancellation
        bookings.remove(bookingNumber - 1);
        Car car = findCarById(toCancel.getCarId());
        if (car != null) {
            car.setAvailable(true);
        }

        System.out.println("\nBooking cancelled successfully!");
        System.out.println("Car " + toCancel.getCarId() + " is now available.");
    }
    private void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet!");
            return;
        }

        System.out.println("\nAll Bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            RegularBooking booking = bookings.get(i);
            System.out.println((i+1) + ". " + booking.getBookingDetails());
        }
    }
    private void addCar() {
        System.out.print("Enter Car ID: ");
        String id = scanner.nextLine();

        if (findCarById(id) != null) {
            System.out.println("Error: Car with this ID already exists!");
            return;
        }

        System.out.print("Enter Car Model: ");
        String model = scanner.nextLine();

        System.out.print("Enter Car Type (regular/electric/luxury): ");
        String type = scanner.nextLine();

        Car newCar = CarFactory.createCar(type, id, model);
        cars.add(newCar);
        System.out.println("Car added successfully! Type: " + newCar.getClass().getSimpleName());
    }
    private void viewCars() {
        if (cars.isEmpty()) {
            System.out.println("No cars available!");
            return;
        }

        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            System.out.println("- ID: " + car.getId() +
                    ", Model: " + car.getModel() +
                    ", Type: " + car.getClass().getSimpleName() +
                    ", Available: " + (car.isAvailable() ? "Yes" : "No"));
        }
    }
    // Helper method to find car by ID
    private Car findCarById(String carId) {
        for (Car car : cars) {
            if (car.getId().equalsIgnoreCase(carId)) {
                return car;
            }
        }
        return null;
    }
}