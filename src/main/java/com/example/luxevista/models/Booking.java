package com.example.luxevista.models;

public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private String bookingDate;
    private int duration;
    private String status;

    // Default constructor
    public Booking() {}

    // Constructor without id
    public Booking(int userId, int roomId, String bookingDate, int duration, String status) {
        this.userId = userId;
        this.roomId = roomId;
        this.bookingDate = bookingDate;
        this.duration = duration;
        this.status = status;
    }

    // Constructor with id
    public Booking(int id, int userId, int roomId, String bookingDate, int duration, String status) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.bookingDate = bookingDate;
        this.duration = duration;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
