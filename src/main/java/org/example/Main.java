package org.example;
import org.example.auth.Admin;
import org.example.db.DatabaseManager;
import org.example.menuUI.Menu;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("**************************************************\n");
        System.out.print("Hello and welcome to Rent a car management system!\n");
        System.out.println("by Syed Fraz Naqvi");
        System.out.print("**************************************************\n");

        Scanner scanner = new Scanner(System.in);
        Admin admin = Admin.getInstance();
        int attempts = 3;
        boolean loggedIn = false;

        while (attempts > 0 && !loggedIn) {
            System.out.println("--- Rent-a-Car Login ---");
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (admin.login(username, password)) {
                loggedIn = true;
                System.out.println("Login successful! Welcome to the system.");
            } else {
                attempts--;
                System.out.println("Invalid credentials. Attempts left: " + attempts);
            }
        }

        if (!loggedIn) {
            System.out.println("No more attempts. Exiting...");
            System.exit(0);
        }
        Menu menu = new Menu();
        menu.showMainMenu();

    }
}