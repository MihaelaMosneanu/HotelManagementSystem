package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    void addEmployee(Employee employee);

    List<Employee> findAll();

    void updateEmployee(int id, Employee employee);

    void deleteEmployee(int id);

    Employee findEmployeeById(int id);
}
