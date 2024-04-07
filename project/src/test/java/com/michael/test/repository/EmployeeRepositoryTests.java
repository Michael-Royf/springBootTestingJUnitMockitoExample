package com.michael.test.repository;

import com.michael.test.entity.Employee;
import com.michael.test.integration.AbstractionContainerBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTests extends AbstractionContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    private String first_name = "Michael";
    private String last_Name = "Royf";
    private String email = "michael@gmail.com";

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName(first_name)
                .lastName(last_Name)
                .email(email)
                .build();
    }


    @DisplayName("JUnit test for save employee method")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup

        // when -action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
        assertThat(savedEmployee.getEmail()).isEqualTo(email);
    }


    @DisplayName("JUnit test for exists by email method")
    @Test
    public void givenEmployeeObject_whenCheckEmail_thenReturnTrue() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Boolean existsByEmail = employeeRepository.existsByEmail(employee.getEmail());
        //then - verify the output
        assertThat(existsByEmail).isTrue();
    }



    @DisplayName("JUnit test for get all employees method")
    @Test
    public void givenEmployeeList_whenFindAllEmployee_thenReturnEmployeesList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Anna")
                .lastName("Karenina")
                .email("anna@gmail.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        // when -action or the behavior we are going to test
        List<Employee> employees = employeeRepository.findAll();
        //then - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
        assertThat(employees.get(0).getEmail()).isEqualTo(email);
        assertThat(employees.get(1).getEmail()).isEqualTo("anna@gmail.com");
    }


    @DisplayName("JUnit test for get employee by id method")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);
    }

    @DisplayName("JUnit test for get employee by email method")
    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeRepository.findByEmail(email).get();
        //then - verify the output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getEmail()).isEqualTo(email);
        assertThat(employeeDB).isEqualTo(employee);
    }

    @DisplayName("JUnit test for update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
        String newFirstName = "MICHAEL";
        String newLastName = "ROYF";
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();
        employeeDB.setFirstName(newFirstName);
        employeeDB.setLastName(newLastName);
        Employee updatedEmployee = employeeRepository.save(employeeDB);
        //then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo(newFirstName);
        assertThat(updatedEmployee.getLastName()).isEqualTo(newLastName);
    }

    @DisplayName("JUnit test for delete employee method")
    @Test
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        Long employeeId = employee.getId();
        // when -action or the behavior we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeDb = employeeRepository.findById(employeeId);
        //then - verify the output
        assertThat(employeeDb).isEmpty();
    }


    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeRepository.findByJPQL(first_name, last_Name);
        //then - verify the output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);
    }

    @DisplayName("JUnit test for custom query using JPQL named params")
    @Test
    public void givenFirstAndLastName_whenFindByNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeRepository.findByJPQLNamedParams(first_name, last_Name);
        //then - verify the output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);
    }

    @DisplayName("JUnit test for custom query using SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeRepository.findByNativeSQL(first_name, last_Name);
        //then - verify the output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);
    }

    @DisplayName("JUnit test for custom query using SQL named params")
    @Test
    public void givenFirstAndLastName_whenFindByNativeSQLWithNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        employee = employeeRepository.save(employee);
        // when -action or the behavior we are going to test
        Employee employeeDB = employeeRepository.findByNativeSQLNamed(first_name, last_Name);
        //then - verify the output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);
    }

}
