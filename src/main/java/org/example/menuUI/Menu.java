package org.example.menuUI;

import org.example.model.*;
import org.example.booking.*;
import org.example.db.CarRepository;
import org.example.db.BookingRepository;
import java.util.Scanner;
import java.util.List;

public class Menu {
    private final Scanner scanner;
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.carRepository = new CarRepository();
        this.bookingRepository = new BookingRepository();
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

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> bookCar();
                case 2 -> viewBookings();
                case 3 -> addCar();
                case 4 -> viewCars();
                case 5 -> cancelBooking();
                case 6 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    private void bookCar() {
        System.out.println("\nAvailable Cars:");
        viewAvailableCars();

        System.out.print("Enter Car ID to book: ");
        String carId = scanner.nextLine().trim();

        Car selectedCar = carRepository.getAllCars().stream()
                .filter(c -> c.getId().equalsIgnoreCase(carId) && c.isAvailable())
                .findFirst()
                .orElse(null);

        if (selectedCar == null) {
            System.out.println("Error: Car not found or already booked!");
            return;
        }

        System.out.print("Enter Your Name: ");
        String customerName = scanner.nextLine().trim();

        System.out.print("Booking Type (regular/premium): ");
        String bookingType = scanner.nextLine().trim().toLowerCase();

        if (!bookingType.equals("regular") && !bookingType.equals("premium")) {
            System.out.println("Invalid booking type! Defaulting to regular.");
            bookingType = "regular";
        }

        RegularBooking booking = BookingFactory.createBooking(bookingType, carId, customerName);
        bookingRepository.addBooking(booking);
        carRepository.updateCarAvailability(carId, false);

        if (booking instanceof PremiumBooking) {
            System.out.print("Enter rental days: ");
            int days;
            try {
                days = Integer.parseInt(scanner.nextLine());
                System.out.printf("Total price: $%.2f%n", booking.getTotalPrice(days));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of days! Setting to 1 day.");
                System.out.printf("Total price: $%.2f%n", booking.getTotalPrice(1));
            }
        }

        System.out.println("Booking created: " + booking.getBookingDetails());
    }

    private void viewBookings() {
        List<RegularBooking> bookings = bookingRepository.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings yet!");
            return;
        }

        System.out.println("\nAll Bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            RegularBooking booking = bookings.get(i);
            System.out.printf("%d. %s%n", i + 1, booking.getBookingDetails());
        }
    }

    private void addCar() {
        System.out.print("\nEnter Car ID: ");
        String id = scanner.nextLine().trim();

        // Check if car already exists
        boolean carExists = carRepository.getAllCars().stream()
                .anyMatch(c -> c.getId().equalsIgnoreCase(id));
        if (carExists) {
            System.out.println("Error: Car with this ID already exists!");
            return;
        }

        System.out.print("Enter Car Model: ");
        String model = scanner.nextLine().trim();

        System.out.print("Enter Car Type (regular/electric/luxury): ");
        String type = scanner.nextLine().trim().toLowerCase();

        if (!type.equals("regular") && !type.equals("electric") && !type.equals("luxury")) {
            System.out.println("Invalid car type! Defaulting to regular.");
            type = "regular";
        }

        Car newCar = CarFactory.createCar(type, id, model);
        carRepository.addCar(newCar);
        System.out.printf("Car added successfully!%nID: %s, Model: %s, Type: %s%n",
                newCar.getId(), newCar.getModel(), newCar.getClass().getSimpleName());
    }

    private void viewCars() {
        List<Car> cars = carRepository.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("\nNo cars available!");
            return;
        }

        System.out.println("\nAll Cars:");
        for (Car car : cars) {
            System.out.printf("- ID: %s, Model: %s, Type: %s, Available: %s%n",
                    car.getId(),
                    car.getModel(),
                    car.getClass().getSimpleName(),
                    car.isAvailable() ? "Yes" : "No");
        }
    }

    private void viewAvailableCars() {
        List<Car> availableCars = carRepository.getAllCars().stream()
                .filter(Car::isAvailable)
                .toList();

        if (availableCars.isEmpty()) {
            System.out.println("No cars currently available!");
            return;
        }

        for (Car car : availableCars) {
            System.out.printf("- ID: %s, Model: %s, Type: %s%n",
                    car.getId(),
                    car.getModel(),
                    car.getClass().getSimpleName());
        }
    }

    private void cancelBooking() {
        List<RegularBooking> bookings = bookingRepository.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings to cancel!");
            return;
        }

        System.out.println("\nCurrent Bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            RegularBooking b = bookings.get(i);
            System.out.printf("%d. %s%n", i + 1, b.getBookingDetails());
        }

        System.out.printf("%nEnter booking number to cancel (1-%d): ", bookings.size());
        int bookingNumber;
        try {
            bookingNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
            return;
        }

        if (bookingNumber < 1 || bookingNumber > bookings.size()) {
            System.out.println("Invalid booking number!");
            return;
        }

        RegularBooking toCancel = bookings.get(bookingNumber - 1);

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
        bookingRepository.deleteBooking(toCancel.getCarId());
        carRepository.updateCarAvailability(toCancel.getCarId(), true);

        System.out.println("\nBooking cancelled successfully!");
        System.out.println("Car " + toCancel.getCarId() + " is now available.");
    }
}