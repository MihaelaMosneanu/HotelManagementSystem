package com.hotelsystem.management.model;

import java.time.LocalDate;

public class Guest {
    private int id;
    private String name;
    private String email;

    private String status;


    public Guest(int id, String name, String email, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;


    }

    public Guest(String guestName, String guestEmail) {
        this.name = guestName;
        this.email = guestEmail;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}