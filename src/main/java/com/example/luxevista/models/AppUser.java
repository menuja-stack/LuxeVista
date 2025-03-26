package com.example.luxevista.models;

public class AppUser {
    private int id;
    private String name;
    private String email;
    private String address;
    private String password;

    // Default constructor
    public AppUser() {}

    // Constructor without id
    public AppUser(String name, String email, String address, String password) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    // Constructor with id
    public AppUser(int id, String name, String email, String address, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}