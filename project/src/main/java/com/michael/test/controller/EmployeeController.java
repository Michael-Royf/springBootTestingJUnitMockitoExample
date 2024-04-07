package com.michael.test.controller;

import com.michael.test.entity.Employee;
import com.michael.test.payload.response.MessageResponse;
import com.michael.test.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.saveEmployee(employee), CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployee() {
        return new ResponseEntity<>(employeeService.getAllEmployee(), OK);
    }

    @GetMapping("/id/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        return new ResponseEntity<>(employeeService.getById(employeeId), OK);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(employeeService.getByEmail(email), OK);
    }


    @PutMapping("/id/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeId") Long employeeId,
                                                   @RequestBody Employee newEmployee) {
        return new ResponseEntity<>(employeeService.updateEmployee(employeeId, newEmployee), OK);
    }

    @DeleteMapping("/id/{employeeId}")
    public ResponseEntity<MessageResponse> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        return new ResponseEntity<>(employeeService.deleteEmployee(employeeId), OK);
    }

}
