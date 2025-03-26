package com.example.luxevista.models;

public class Room {
    private int id;
    private String roomNumber;
    private String roomType;
    private double price;
    private int capacity;
    private String description;
    private boolean isAvailable;
    private String amenities;
    private String imageUrl;

    // Default constructor
    public Room() {}

    // Constructor without id
    public Room(String roomNumber, String roomType, double price, int capacity,
                String description, boolean isAvailable, String amenities, String imageUrl) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.isAvailable = isAvailable;
        this.amenities = amenities;
        this.imageUrl = imageUrl;
    }

    // Constructor with id
    public Room(int id, String roomNumber, String roomType, double price, int capacity,
                String description, boolean isAvailable, String amenities, String imageUrl) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.isAvailable = isAvailable;
        this.amenities = amenities;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}