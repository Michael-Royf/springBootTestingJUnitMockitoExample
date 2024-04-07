package com.michael.test.service;

import com.michael.test.entity.Employee;
import com.michael.test.payload.response.MessageResponse;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployee();

    Employee getById(Long employeeId);

    Employee getByEmail(String email);

    Employee updateEmployee(Long employeeId, Employee newEmployee);

    MessageResponse deleteEmployee(Long employeeId);
}
