package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {

    private Connection dbConnection;

    public PaymentRepositoryImpl(Connection connection) {
        this.dbConnection = connection;
    }

    @Override
    public void addPayment(Payment payment) {
        String insertQuery = "INSERT INTO payments (amount,payment_method) VALUES ( ?,? )";
        try (PreparedStatement statement = dbConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, payment.getAmount());
            statement.setString(2, payment.getPaymentMethod());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                payment.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payment";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int amount = resultSet.getInt("amount");
                String paymentMethod = resultSet.getString("payment_method");

                payments.add(new Payment(id, amount, paymentMethod));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return payments;
    }

    @Override
    public void updatePayment(int id, Payment payment) {
        String updateQuery = "UPDATE payments SET amount = ?, payment_method = ?  WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(updateQuery)) {
            statement.setInt(1, id);
            statement.setDouble(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deletePayment(int id) {
        String deleteQuery = "DELETE FROM payments WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Payment findPaymentById(int id) {
        String query = "SELECT * FROM payments WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int amount = resultSet.getInt("amount");
                String paymentMethod = resultSet.getString("payment_method");
                return new Payment(id, amount, paymentMethod);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}

