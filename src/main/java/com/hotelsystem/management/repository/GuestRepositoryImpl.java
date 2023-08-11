package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryImpl implements GuestRepository {
    private Connection dbConnection;

    public GuestRepositoryImpl(Connection connection) {
        this.dbConnection = connection;

    }


    @Override
    public void addGuest(Guest guest) {
        String insertQuery = "INSERT INTO guests (name, email) VALUES ( ?,?)";
        try (PreparedStatement statement = dbConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, guest.getName());
            statement.setString(2, guest.getEmail());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                guest.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Guest> findAll() {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM guests";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String status = resultSet.getString("status");
                guests.add(new Guest(id, name, email, status));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return guests;
    }

    @Override
    public void updateGuest(Guest guest) {
        String updateQuery = "UPDATE guests SET name = ?, email = ?, status = ?  WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(updateQuery)) {
            statement.setString(1, guest.getName());
            statement.setString(2, guest.getEmail());
            statement.setString(3, guest.getStatus());
            statement.setInt(4, guest.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteGuest(int id) {
        String deleteQuery = "DELETE FROM guests WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Guest findGuestById(int id) {
        String query = "SELECT * FROM guests WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String status = resultSet.getString("status");
                return new Guest(id, name, email, status);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Guest> findAllByName(String guestName) {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM guests WHERE name = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setString(1, guestName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String status = resultSet.getString("status");
                guests.add(new Guest(id, name, email, status));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return guests;

    }

}
