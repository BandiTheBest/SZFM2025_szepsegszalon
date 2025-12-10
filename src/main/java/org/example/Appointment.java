package org.example;

import java.time.LocalDate;

public class Appointment {
    private int id;
    private String serviceName; // Pl: "Masszázs"
    private int userId;
    private LocalDate bookingDate;
    private String bookingTime;

    // Konstruktor beolvasáshoz
    public Appointment(int id, String serviceName, int userId, LocalDate bookingDate, String bookingTime) {
        this.id = id;
        this.serviceName = serviceName;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
    }

    // Konstruktor új foglaláshoz (id nélkül)
    public Appointment(String serviceName, int userId, LocalDate bookingDate, String bookingTime) {
        this(0, serviceName, userId, bookingDate, bookingTime);
    }

    // --- Getters és Setters ---
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getServiceName() { return serviceName; }
    public int getUserId() { return userId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public String getBookingTime() { return bookingTime; }

    @Override
    public String toString() {
        return serviceName + " | " + bookingDate + " (" + bookingTime + ")";
    }
    
}