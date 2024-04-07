package com.michael.test.service;

import com.michael.test.entity.Employee;
import com.michael.test.exceptions.EmployeeNotFoundException;
import com.michael.test.repository.EmployeeRepository;
import com.michael.test.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;  //not an interface

    private Employee employee;
    private Long employeeId = 1L;
    private String firstName = "Michael";
    private String lastName = "Royf";
    private String email = "michael@gmail.com";

    @BeforeEach
    public void setup() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(employeeId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    @DisplayName("JUnit test for save employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.existsByEmail(email)).willReturn(Boolean.FALSE);
        given(employeeRepository.save(employee)).willReturn(employee);
        // when -action or the behavior we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employee);
    }

    @DisplayName("Junit test for save Employee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenReturnThrowsException() {
        //given - precondition or setup
        given(employeeRepository.existsByEmail(email)).willReturn(Boolean.TRUE);
        // when -action or the behavior we are going to test
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        //then - verify the output
        //проверяем, что метод сохранения в базу данных не запустится
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for get all employee method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Anna")
                .lastName("Karenina")
                .email("anna@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));
        // when -action or the behavior we are going to test
        List<Employee> employeeList = employeeService.getAllEmployee();
        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList.get(0)).isEqualTo(employee);
        assertThat(employeeList.get(1)).isEqualTo(employee1);
    }


    @DisplayName("JUnit test for get all employee method(negative scenario)")
    @Test
    public void givenEmployeeList_whenGetEmployee_thenReturnEmptyCollection() {
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        // when -action or the behavior we are going to test
        List<Employee> employeeList = employeeService.getAllEmployee();
        //then - verify the output
        assertThat(employeeList).isEmpty();
    }

    @DisplayName("JUnit test for get employee by id method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        // when -action or the behavior we are going to test
        Employee savedEmployee = employeeService.getById(employeeId);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employee);
        verify(employeeRepository).findById(employeeId);
    }

    @DisplayName("JUnit test for get employee by id method(negative scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnError() {
        //given - precondition or setup
        given(employeeRepository.findById(anyLong())).willReturn(Optional.empty());
        // when -action or the behavior we are going to test
        Assertions.assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getById(employeeId));
        //then - verify the output
        verify(employeeRepository).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @DisplayName("JUnit test for get employee by email method")
    @Test
    public void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(email)).willReturn(Optional.of(employee));
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeService.getByEmail(email);
        //then - verify the output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);
        verify(employeeRepository).findByEmail(email);
    }


    @DisplayName("JUnit test for get employee by email method(negative scenario)")
    @Test
    public void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnError() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(email)).willReturn(Optional.empty());
        // when -action or the behavior we are going to test
        Assertions.assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getByEmail(email));
        //then - verify the output
        verify(employeeRepository).findByEmail(email);
        verifyNoMoreInteractions(employeeRepository);
    }


    @DisplayName("JUnit test for update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
        //   given(employeeRepository.save(employee)).willReturn(employee);
        Employee newEmployee = Employee.builder()
                .firstName("MICHAEL")
                .lastName("ROYF")
                .email("newEmail@gmail.com")
                .build();
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(newEmployee);
        // when -action or the behavior we are going to test
        Employee result = employeeService.updateEmployee(employeeId, newEmployee);
        //then - verify the output'
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).save(employee);
        Assertions.assertEquals(newEmployee, result);
        Assertions.assertEquals(newEmployee.getFirstName(), result.getFirstName());
        Assertions.assertEquals(newEmployee.getLastName(), result.getLastName());
    }


    @DisplayName("JUnit test for delete employee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnMessage() {
        //given - precondition or setup
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        willDoNothing().given(employeeRepository).delete(employee);
        // when -action or the behavior we are going to test
        employeeService.deleteEmployee(employeeId);
        //then - verify the output
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository, times(1)).delete(employee);
    }


}
