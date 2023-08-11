package com.hotelsystem.management.model;

import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String address;
    private List<Room> rooms;
    private List<Employee> employees;

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Hotel(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;

    }

    public void setId(int anInt) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }


    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }


}
