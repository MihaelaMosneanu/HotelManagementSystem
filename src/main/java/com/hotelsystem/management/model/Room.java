package com.hotelsystem.management.model;

public class Room {
    private int id;
    private int room_number;
    private int floor;
    private String room_type;
    private double price_per_night;
    private String available;
    private int hotel_id;


    public Room(int id,int room_number, int floor, String room_type, double price_per_night, String available, int hotel_id) {
        this.id = id;
        this.room_number = room_number;
        this.floor = floor;
        this.room_type = room_type;
        this.price_per_night = price_per_night;
        this.available = available;
        this.hotel_id = hotel_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return room_number;
    }

    public void setRoomNumber(int room_number) {
        this.room_number = room_number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getRoomType() {
        return room_type;
    }

    public void setRoomType(String room_type) {
        this.room_type = room_type;
    }

    public double getPricePerNight() {
        return price_per_night;
    }

    public void setPricePerNight(double price_per_night) {
        this.price_per_night = price_per_night;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public int getHotelId() {
        return hotel_id;
    }

    public void setHotelId(int hotel_id) {
        this.hotel_id = hotel_id;
    }
}
