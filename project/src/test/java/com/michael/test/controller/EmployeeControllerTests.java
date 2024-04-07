package com.michael.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michael.test.entity.Employee;
import com.michael.test.exceptions.EmployeeNotFoundException;
import com.michael.test.payload.response.MessageResponse;
import com.michael.test.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private Long employeeId = 1L;
    private String first_name = "Michael";
    private String last_Name = "Royf";
    private String email = "michael@gmail.com";

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(employeeId)
                .firstName(first_name)
                .lastName(last_Name)
                .email(email)
                .build();
    }


    @DisplayName("JUnit test for create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when -action or the behavior we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );
        //then - verify the output
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("JUnit test for get all employees REST API")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Anna")
                .lastName("Karenina")
                .email("anna@gmail.com")
                .build();
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(employee1);
        given(employeeService.getAllEmployee()).willReturn(employeeList);
        // when -action or the behavior we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employee"));
        //then - verify the output
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }


    @DisplayName("JUnit test for get employee by id REST API(positive scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        given(employeeService.getById(employeeId)).willReturn(employee);

        // when -action or the behavior we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employee/id/{employeeId}", employeeId));
        //then - verify the output
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("JUnit test for get Employee by id REST API(negative scenario)")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnError() throws Exception {
        //given - precondition or setup
        Long invalidEmployeeId = 999L;
        given(employeeService.getById(invalidEmployeeId))
                .willThrow(new EmployeeNotFoundException(String.format("Employee with id: %s not found", invalidEmployeeId)));
        // when -action or the behavior we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employee/id/{employeeId}", invalidEmployeeId)
                .contentType(MediaType.APPLICATION_JSON));
        //then - verify the output
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DisplayName("JUnit test for update employee REST API(positive scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //given - precondition or setup

        Employee newEmployee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("michael@gmail.com")
                .build();


        //   given(employeeService.getById(employeeId)).willReturn(employee);
        given(employeeService.updateEmployee(employeeId, newEmployee)).willReturn(newEmployee);

        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employee/id/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee))
        );
        //then - verify the output
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    //TODO: dont work!
    @DisplayName("JUnit test for update Employee REST API (negative scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        Long invalidEmployeeId = 9999L;
        Employee employeeUpdate = Employee.builder()
                .firstName("MICHAEL")
                .lastName("ROYF")
                .email("michael@gmail.com")
                .build();
        given(employeeService.getById(invalidEmployeeId))
                .willThrow(new EmployeeNotFoundException(String.format("Employee with id: %s not found", invalidEmployeeId)));
//        given(employeeService.updateEmployee(invalidEmployeeId, employeeUpdate))
//                .willThrow(EmployeeNotFoundException.class);
        //    .willThrow(new EmployeeNotFoundException(String.format("Employee with id: %s not found", invalidEmployeeId)));

        // When - Action or the behavior we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employee/id/{employeeId}", invalidEmployeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeUpdate))
        );

        // Then - Verify the output
        response
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        //            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException));
    }


    @DisplayName("JUnit test for delete employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnMessage() throws Exception {
        //given - precondition or setup
        MessageResponse message = new MessageResponse(String.format("Employee with id: %s was deleted", employeeId));

        given(employeeService.getById(employeeId)).willReturn(employee);
        given(employeeService.deleteEmployee(employeeId))
                .willReturn(message);
        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employee/id/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(message))
        );

        //then - verify the output
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message.getMessage())));
    }

}



