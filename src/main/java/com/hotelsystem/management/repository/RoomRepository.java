package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Room;

import java.util.List;

public interface RoomRepository {

    void addRoom(Room room);

    List<Room> findAll();

    void updateRoom(int id, Room room);

    void deleteRoom(int id);

    Room findRoomById(int id);

    List<Room> findAllByHotelId(int id);

}
