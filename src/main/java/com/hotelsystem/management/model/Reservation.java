package com.hotelsystem.management.model;

import java.sql.Date;
import java.time.LocalDate;

public class Reservation {
    private int id;
    private int reservation_id;
    private LocalDate check_in_date;
    private LocalDate check_out_date;
    private int allocated_room;
    private int room_id;
    private int payment_id;
    private int guest_id;

    public Reservation(int id,int reservation_id, LocalDate check_in_date, LocalDate check_out_date, int allocated_room, int room_id, int payment_id, int guest_id) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.allocated_room = allocated_room;
        this.room_id = room_id;
        this.payment_id = payment_id;
        this.guest_id = guest_id;
    }

    public Reservation(int reservation_id, LocalDate check_in_date, LocalDate check_out_date, int allocated_room, int room_id, int payment_id, int guest_id) {
        this.reservation_id = reservation_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.allocated_room = allocated_room;
        this.room_id = room_id;
        this.payment_id = payment_id;
        this.guest_id = guest_id;
    }

    public Reservation(int reservationNumber, LocalDate checkInDate, LocalDate checkOutDate, int selectedRoom, Guest guest) {
        this.reservation_id = reservationNumber;
        this.check_in_date = checkInDate;
        this.check_out_date = checkOutDate;
        this.allocated_room = selectedRoom;
        this.guest_id = guest.getId();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservation_id;
    }

    public void setReservationId(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public LocalDate getCheckInDate() {
        return check_in_date;
    }

    public void setCheckInDate(LocalDate check_in_date) {
        this.check_in_date = check_in_date;
    }

    public LocalDate getCheckOutDate() {
        return check_out_date;
    }

    public void setCheckOutDate(LocalDate check_out_date) {
        this.check_out_date = check_out_date;
    }

    public int getAllocatedRoom() {
        return allocated_room;
    }

    public void setAllocatedRoom(int allocated_room) {
        this.allocated_room = allocated_room;
    }

    public int getRoomId() {
        return room_id;
    }

    public void setRoomId(int room_id) {
        this.room_id = room_id;
    }

    public int getPaymentId() {
        return payment_id;
    }

    public void setPaymentId(Payment payment) {
        this.payment_id = payment_id;
    }

    public int getGuestId() {
        return guest_id;
    }

    public void setGuestId(int guest_id) {
        this.guest_id = guest_id;
    }
}