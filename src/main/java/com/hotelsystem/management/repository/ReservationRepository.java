package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Reservation;

import java.util.List;

public interface ReservationRepository {
    void addReservation(Reservation reservation);

    List<Reservation> findAll();

    void updateReservation(int id, Reservation reservation);

    void deleteReservation(int id);

    Reservation findReservationById(int id);

    List<Reservation> findReservationsForGuest(int guestId);

    Reservation findReservationByReservationNumber(int reservationNumber);
}
