package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class RoomRepositoryImpl implements RoomRepository {
    private Connection dbConnection;

    public RoomRepositoryImpl(Connection connection) {
        this.dbConnection = connection;
    }


    @Override
    public void addRoom(Room room) {
        String insertQuery = "INSERT INTO rooms (room_number,floor,room_type,price_per_night,available,hotel_id) VALUES ( ?,?,?,?,?,? )";
        try (PreparedStatement statement = dbConnection.prepareStatement(insertQuery)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setInt(2, room.getFloor());
            statement.setString(2, room.getRoomType());
            statement.setDouble(3, room.getPricePerNight());
            statement.setString(4, room.getAvailable());
            statement.setInt(4, room.getHotelId());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                room.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM room";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int room_number = resultSet.getInt("room_number");
                int floor = resultSet.getInt("floor");
                String room_type = resultSet.getString("room_type");
                double price_per_night = resultSet.getInt("price_per_night");
                String available =resultSet.getString("available");
                int hotel_id = resultSet.getInt("hotel_id");
                rooms.add(new Room(id,room_number, floor, room_type, price_per_night, available, hotel_id));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rooms;
    }

    @Override
    public void updateRoom(int id, Room room) {
        String updateQuery = "UPDATE rooms SET room_number = ?, floor = ?, room_type = ?, price_per_night = ?,available= ?,hotel_id = ? WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(updateQuery)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setInt(2, room.getFloor());
            statement.setString(3, room.getRoomType());
            statement.setDouble(4, room.getPricePerNight());
            statement.setString(5, room.getAvailable());
            statement.setInt(6, room.getHotelId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteRoom(int id) {
        String deleteQuery = "DELETE FROM rooms WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Room findRoomById(int id) {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int room_number = resultSet.getInt("room_number");
                int floor = resultSet.getInt("floor");
                String room_type = resultSet.getString("room_type");
                double price_per_night = resultSet.getDouble("price_per_night");
                String available = resultSet.getString("available");
                int hotel_id = resultSet.getInt("hotel_id");
                return new Room(id,room_number, floor, room_type, price_per_night, available, hotel_id);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Room> findAllByHotelId(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE hotel_id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, hotelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int roomId = resultSet.getInt("id");
                    int roomNumber = resultSet.getInt("room_number");
                    int floor = resultSet.getInt("floor");
                    String roomType = resultSet.getString("room_type");
                    double pricePerNight = resultSet.getDouble("price_per_night");
                    String available = resultSet.getString("available");
                    rooms.add(new Room(roomId, roomNumber, floor, roomType, pricePerNight, available, hotelId));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rooms;
    }

}

