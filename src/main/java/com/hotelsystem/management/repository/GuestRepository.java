package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Guest;

import java.util.List;

public interface GuestRepository {
    void addGuest(Guest guest) ;

    List<Guest> findAll() ;

    void updateGuest(Guest guest) ;

    void deleteGuest(int id) ;

    Guest findGuestById(int id) ;

    List<Guest> findAllByName(String guestName);

}
