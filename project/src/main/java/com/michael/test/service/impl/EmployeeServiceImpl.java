package com.michael.test.service.impl;

import com.michael.test.entity.Employee;
import com.michael.test.exceptions.EmployeeNotFoundException;
import com.michael.test.payload.response.MessageResponse;
import com.michael.test.repository.EmployeeRepository;
import com.michael.test.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    @Override
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new EmployeeNotFoundException(String.format("Employee with email: %s already exists", employee.getEmail()));
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }


    @Override
    public Employee getById(Long employeeId) {
        return findEmployeeByIdInDB(employeeId);
    }

    @Override
    public Employee getByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee with email: %s not found", email)));
    }


    @Override
     public Employee updateEmployee(Long employeeId, Employee newEmployee) {
        Employee employee = findEmployeeByIdInDB(employeeId);
        employee.setFirstName(newEmployee.getFirstName());
        employee.setLastName(newEmployee.getLastName());
        return employeeRepository.save(employee);
    }


    @Override
    public MessageResponse deleteEmployee(Long employeeId) {
        Employee employee = findEmployeeByIdInDB(employeeId);
        employeeRepository.delete(employee);
        return new MessageResponse(String.format("Employee with id: %s was deleted", employeeId));
    }

    private Employee findEmployeeByIdInDB(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee with id: %s not found", employeeId)));
    }

}
