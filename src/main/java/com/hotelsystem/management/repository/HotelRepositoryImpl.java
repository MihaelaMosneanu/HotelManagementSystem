package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelRepositoryImpl implements HotelRepository {
    private Connection dbConnection;

    public HotelRepositoryImpl(Connection connection) {
        this.dbConnection = connection;
    }

    @Override
    public void addHotel(Hotel hotel) {
        String insertQuery = "INSERT INTO hotels (name, address) VALUES ( ?,? )";
        try (PreparedStatement statement = dbConnection.prepareStatement(insertQuery)) {
            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getAddress());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                hotel.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Hotel> findAll() {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT * FROM hotels";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");

                hotels.add(new Hotel(id, name, address));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return hotels;
    }

    @Override
    public void updateHotel(int id, Hotel hotel) {
        String updateQuery = "UPDATE hotels SET name = ?, address = ?  WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(updateQuery)) {
            statement.setInt(1, id);
            statement.setString(2, hotel.getName());
            statement.setString(3, hotel.getAddress());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteHotel(int id) {
        String deleteQuery = "DELETE FROM hotels WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Hotel findHotelById(int id) {
        String query = "SELECT * FROM hotels WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                return new Hotel(id, name, address);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}

