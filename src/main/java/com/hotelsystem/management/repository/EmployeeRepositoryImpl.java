package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private Connection dbConnection;

    public EmployeeRepositoryImpl(Connection connection) {
        this.dbConnection = connection;
    }

    public void addEmployee(Employee employee) {

        String insertQuery = "INSERT INTO employees (name, position,email,hotel_id) VALUES ( ?,?,?,? )";
        try (PreparedStatement statement = dbConnection.prepareStatement(insertQuery)) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getPosition());
            statement.setString(3, employee.getEmail());
            statement.setInt(4, employee.getHotelId());


            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                employee.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                String email = resultSet.getString("email");
                int hotel_id = resultSet.getInt("hotel_id");
                employees.add(new Employee(id, name, position, email, hotel_id));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return employees;
    }

    public void updateEmployee(int id, Employee employee) {
        String updateQuery = "UPDATE employees SET name = ?, position = ?, email = ? ,hotel_id= ? WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(updateQuery)) {
            statement.setInt(1, id);
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getPosition());
            statement.setString(4, employee.getEmail());
            statement.setInt(5, employee.getHotelId());

            statement.executeUpdate();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteEmployee(int id)  {
        String deleteQuery = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public Employee findEmployeeById(int id)  {

        String query = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String position = resultSet.getString("position");
                    String email = resultSet.getString("email");
                    int hotel_id = resultSet.getInt("hotel_id");
                    return new Employee(id, name, position, email,hotel_id);
                }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}


