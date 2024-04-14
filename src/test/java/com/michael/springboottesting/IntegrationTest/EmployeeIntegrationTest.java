package com.michael.springboottesting.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import com.michael.springboottesting.enums.EmployeeStatus;
import com.michael.springboottesting.enums.Gender;
import com.michael.springboottesting.model.Address;
import com.michael.springboottesting.model.Employee;
import com.michael.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    private Employee employee1;


    private EmployeeDto employeeDto;

    private EmployeeDto employeeDto1;

    @BeforeEach
    public void setUp() {
        employeeDto = EmployeeDto.builder()
                .firstName("Michael")
                .lastName("John")
                .email("MichaelJohn@gmail.com")
                .phone("8125876597")
                .address(new Address("Goodnews estate, Ogombo Road", "Lekki", "Lagos", "1234", "Nigeria"))
                .build();
        employee = Employee.builder()
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .gender(Gender.MALE)
                .status(EmployeeStatus.ACTIVE)
                .phoneNumber( employeeDto.getPhone())
                .address(Arrays.asList(employeeDto.getAddress()))
                .build();

        employeeDto = EmployeeDto.builder()
                .firstName("Michael")
                .lastName("John")
                .email("MichaelJohn@gmail.com")
                .phone("8125876597")
                .address(new Address("Goodnews estate, Ogombo Road", "Lekki", "Lagos", "1234", "Nigeria"))
                .build();
        employee1 = Employee.builder()
                .firstName(employeeDto1.getFirstName())
                .lastName(employeeDto1.getLastName())
                .email(employeeDto1.getEmail())
                .gender(Gender.MALE)
                .status(EmployeeStatus.ACTIVE)
                .phoneNumber( employeeDto1.getPhone())
                .address(Arrays.asList(employeeDto1.getAddress()))
                .build();

        employeeRepository.deleteAll();


    }


    @DisplayName("Test for employee save end point")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSaveEmployee() throws Exception {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        ResultActions result = mockMvc.perform(post("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)));
        //then - verify the output
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @DisplayName("Test for list of employee end point")
    @Test
    public void givenListOfEmployee_whenGetAllEmployee_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = List.of(employee1, employee);
        employeeRepository.saveAll(employeeList);

        //when - action or behaviour that we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employee"));

        //then - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));

    }

    @DisplayName("Test for get employee by id")
    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup

        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employee/{id}", 10L));

        //then - verify the output
        resultActions.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @DisplayName("Test for invalid employee by id")
    @Test
    public void givenInvalidEmployeeId_whenFindById_thenReturnEmpty() throws Exception {
        //given - precondition or setup

        employeeRepository.save(employee1);
        //when - action or behaviour that we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employee/{id}", employee.getId()));

        //then - verify the output
        resultActions.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void givenEmployeeIdAndObject_whenUpdateEmployee_thenReturnEmployeeDto() throws Exception {
        // Given
        employeeRepository.save(employee);
        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employee1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employee1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee1.getEmail()));

    }

}
