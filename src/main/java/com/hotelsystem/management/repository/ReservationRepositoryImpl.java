package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {
    private Connection dbConnection;

    public ReservationRepositoryImpl(Connection connection) {
        this.dbConnection = connection;
    }

    @Override
    public void addReservation(Reservation reservation) {
        String insertQuery = "INSERT INTO reservations " +
                "(reservation_id, check_in_date, check_out_date, allocated_room, room_id, payment_id, guest_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = dbConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, reservation.getReservationId());
            statement.setDate(2, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(3, Date.valueOf(reservation.getCheckOutDate()));
            statement.setInt(4, reservation.getAllocatedRoom());
            statement.setInt(5, reservation.getRoomId());
            statement.setInt(6, reservation.getPaymentId());
            statement.setInt(7, reservation.getGuestId());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                reservation.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int reservation_id = resultSet.getInt("reservation_id");
                LocalDate check_in_date = resultSet.getDate("check_in_date").toLocalDate();
                LocalDate check_out_date = resultSet.getDate("check_out_date").toLocalDate();
                int allocated_room = resultSet.getInt("allocated_room");
                int room_id = resultSet.getInt("room_id");
                int payment_id = resultSet.getInt("payment_id");
                int guest_id = resultSet.getInt("guest_id");

                reservations.add(new Reservation(id, reservation_id, check_in_date, check_out_date, allocated_room, room_id, payment_id, guest_id));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    @Override
    public void updateReservation(int id, Reservation reservation) {
        String updateQuery = "UPDATE reservations SET reservation_id = ?, check_in_date = ?, check_out_date = ?, allocated_room = ?, room_id = ?, payment_id = ?, guest_id = ? WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(updateQuery)) {
            statement.setInt(1, reservation.getReservationId());
            statement.setDate(2, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(3, Date.valueOf(reservation.getCheckOutDate()));
            statement.setString(4, String.valueOf(reservation.getAllocatedRoom()));
            statement.setInt(5, reservation.getRoomId());
            statement.setInt(6, reservation.getPaymentId());
            statement.setInt(7, reservation.getGuestId());
            statement.setInt(8, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void deleteReservation(int id) {
        String deleteQuery = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Reservation findReservationById(int id) {
        String query = "SELECT * FROM reservations WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int reservation_id = resultSet.getInt("reservation_id");
                LocalDate check_in_date = resultSet.getDate("check_in_date").toLocalDate();
                LocalDate check_out_date = resultSet.getDate("check_out_date").toLocalDate();
                int allocated_room = resultSet.getInt("allocated_room");
                int room_id = resultSet.getInt("room_id");
                int payment_id = resultSet.getInt("payment_id");
                int guest_id = resultSet.getInt("guest_id");
                return new Reservation(id, reservation_id, check_in_date, check_out_date, allocated_room, room_id, payment_id, guest_id);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reservation> findReservationsForGuest(int guestId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE guest_id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, guestId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int reservation_id = resultSet.getInt("reservation_id");
                LocalDate check_in_date = resultSet.getDate("check_in_date").toLocalDate();
                LocalDate check_out_date = resultSet.getDate("check_out_date").toLocalDate();
                int allocated_room = resultSet.getInt("allocated_room");
                int room_id = resultSet.getInt("room_id");
                int payment_id = resultSet.getInt("payment_id");
                int guest_id = resultSet.getInt("guest_id");


                reservations.add(new Reservation(id, reservation_id, check_in_date, check_out_date, allocated_room, room_id, payment_id, guest_id));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    @Override
    public Reservation findReservationByReservationNumber(int reservationNumber) {
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, reservationNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int reservation_id = resultSet.getInt("reservation_id");
                LocalDate check_in_date = resultSet.getDate("check_in_date").toLocalDate();
                LocalDate check_out_date = resultSet.getDate("check_out_date").toLocalDate();
                int allocated_room = resultSet.getInt("allocated_room");
                int room_id = resultSet.getInt("room_id");
                int payment_id = resultSet.getInt("payment_id");
                int guest_id = resultSet.getInt("guest_id");
                return new Reservation(id, reservation_id, check_in_date, check_out_date, allocated_room, room_id, payment_id, guest_id);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
