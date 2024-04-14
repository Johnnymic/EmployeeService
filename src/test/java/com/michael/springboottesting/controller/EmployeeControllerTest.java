package com.michael.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import com.michael.springboottesting.model.Employee;
import com.michael.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;

import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import  static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    private  Employee employee1;


    private  EmployeeDto employeeDto;

    private EmployeeDto employeeDto1;

    @BeforeEach
    public void setUp()
    {
        employee = Employee.builder()
                .id(1L)
                .firstName("Michael")
                .lastName("John")
                .email("MichaelJohn@gmail.com")
                .build();

          employee1 = Employee.builder()
                    .id(2L)
                    .firstName("Peter")
                    .lastName("Johnny")
                    .email("MichaelJohn@gmail.com")
                    .build();

        employeeDto = EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
        employeeDto1 = EmployeeDto.builder()
                .id(employee1.getId())
                .firstName(employee1.getFirstName())
                .lastName(employee1.getLastName())
                .email(employee1.getEmail())
                .build();



    }
   ObjectMapper objectMapperr = new ObjectMapper().registerModule(new JavaTimeModule());
    /*
    *first thing to do is to get a template and next thing is to write the method , such that given when verify
    * employee service to call the method and that method is going to receive request coming from the Employee class
    * and will return those argument on after the other. if i mockMvc method to consume the Api , content is jsona
    */

 @DisplayName("Test for employee save end point")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSaveEmployee () throws Exception {
      //given - precondition or setup
   given(employeeService.saveEmployee(any(EmployeeDto.class))).willAnswer((Invocation)-> Invocation.getArgument(0));
      //when - action or behaviour that we are going to test
      ResultActions result = mockMvc.perform(post("/api/v1/employee")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(employeeDto)));
      //then - verify the output
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                .andExpect(jsonPath("$.email",is(employee.getEmail())));
       }
       @DisplayName("Test for list of employee end point")
       @Test
       public void givenlistOfEmployee_whenGetAllEmployee_thenReturnEmployeeList () throws Exception {
         //given - precondition or setup
           List<EmployeeDto> employeeList = List.of(employeeDto,employeeDto1);

      given(employeeService.getAllEmployee()).willReturn(employeeList);
         //when - action or behaviour that we are going to test
          ResultActions resultActions = mockMvc.perform(get("/api/v1/employee"));

         //then - verify the output
           resultActions.andExpect(status().isOk())
                   .andDo(print())
                   .andExpect(jsonPath("$.size()",is(employeeList.size())));

       }

       @DisplayName("Test for get employee by id")
       @Test
       public void givenEmployeeId_whenFindById_thenReturnEmployeeObject () throws Exception {
         //given - precondition or setup

         given(employeeService.findEmployeeById(employee.getId())).willReturn(Optional.of(employeeDto));

         //when - action or behaviour that we are going to test
         ResultActions resultActions = mockMvc.perform(get("/api/v1/employee/{id}",employee.getId() ));

         //then - verify the output
           resultActions.andExpect(status().isOk()).andDo(print())
                   .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                   .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                   .andExpect(jsonPath("$.email",is(employee.getEmail())));

       }

    @DisplayName("Test for invalid employee by id")
    @Test
    public void givenInvalidEmployeeId_whenFindById_thenReturnEmpty () throws Exception {
        //given - precondition or setup

        given(employeeService.findEmployeeById(employee.getId())).willReturn(Optional.empty());

        //when - action or behaviour that we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employee/{id}",employee.getId() ));

        //then - verify the output
        resultActions.andExpect(status().isNotFound()).andDo(print());
 }

    @DisplayName("Test for update employee by id")
    @Test
    public void givenEmployeeIdAndObject_whenUpdate_thenReturnEmployee () throws Exception {
        //given - precondition or setup


       given(employeeService.updateEmployee(1L,employeeDto)).willAnswer((p)->p.getArgument(0));
       //when - action or behaviour that we are going to test
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employee/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)));;

        //then - verify the output
        resultActions.andExpect(status().isOk()).andDo(print());




    }
    @Test
    public void givenEmployeeIdAndObject_whenUpdateEmployee_thenReturnEmployeeDto() throws Exception {
        // Given


        given(employeeService.updateEmployee(employeeDto1.getId(), employeeDto1)).willReturn(employeeDto1);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/{id}" ,employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk())
              //  .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employee1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employee1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee1.getEmail()));

        // Then
        verify(employeeService).updateEmployee(employee1.getId(), employeeDto1);
    }






}
