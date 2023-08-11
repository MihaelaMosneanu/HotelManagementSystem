package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Hotel;

import java.util.List;

public interface HotelRepository {
    void addHotel(Hotel hotel);

    List<Hotel> findAll() ;

    void updateHotel(int id, Hotel hotel) ;

    void deleteHotel(int id) ;

    Hotel findHotelById(int id);
}
