package com.hotelsystem.management.model;


public class Employee {
    private int id;
    private String name;
    private String position;
    private String email;
    private int hotel_id;

    public Employee() {

    }

    public Employee(int id, String name, String position, String email, int hotel_id) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
        this.hotel_id = hotel_id;
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


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHotelId() {
        return hotel_id;
    }

    public void setHotelId(int hotel_id) {
        this.hotel_id = hotel_id;
    }

}
