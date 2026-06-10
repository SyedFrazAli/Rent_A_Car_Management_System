# Rent A Car Management System

A Java-based car rental management system built as part of the Advanced Software Engineering module (MSc Software Engineering, University of Greater Manchester).

## Overview

This project demonstrates Object-Oriented Programming (OOP) principles and software design patterns in a real-world management system. The system models a car rental business, handling vehicles, customers, bookings, and billing with a persistent SQLite database.

## Design Patterns Applied

- **Factory Pattern** — creates different vehicle type instances
- **Singleton Pattern** — manages the central database connection
- **Strategy Pattern** — pluggable pricing calculation strategies
- **Observer Pattern** — booking status change notifications

## Tech Stack

- **Language:** Java
- **Database:** SQLite (via JDBC)
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA

## Features

- Add, update, and remove vehicles from the fleet
- Register and manage customer accounts
- Create and track rental bookings
- Calculate costs by vehicle type and rental duration
- Persistent storage via SQLite

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
git clone https://github.com/SyedFrazAli/Rent_A_Car_Management_System.git
cd Rent_A_Car_Management_System
mvn clean install
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## Project Structure

```
src/main/java/org/example/
├── models/       # Domain entities (Vehicle, Customer, Booking)
├── patterns/     # Design pattern implementations
├── services/     # Business logic layer
└── Main.java     # Entry point
```

## Module

Built for the Advanced Software Engineering module — MSc Software Engineering, University of Greater Manchester.
