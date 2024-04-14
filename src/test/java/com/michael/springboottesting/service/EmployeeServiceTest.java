package com.michael.springboottesting.service;

import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import com.michael.springboottesting.exceptions.ResourceNotFoundException;
import com.michael.springboottesting.model.Employee;
import com.michael.springboottesting.repository.EmployeeRepository;
import  static  org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

@ExtendWith(MockitoExtension.class)

public class EmployeeServiceTest {
   @Mock
    private EmployeeRepository employeeRepository;

   @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    private EmployeeDto employeeDto;

    @BeforeEach
    public void setup()
    {
        employeeDto = EmployeeDto.builder()
                .id(1L)
                .firstName("Michael")
                .lastName("John")
                .email("MichaelJohn@gmail.com")
                .build();
        employee = Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();


    }
    @DisplayName("Test for save employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSaveEmployee (){
      //given - precondition or setup
        given(employeeRepository.findByEmail(employeeDto.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(any(Employee.class))).willReturn(employee);
        System.out.println(employeeRepository);
        System.out.println(employeeService);

      //when - action or behaviour that we are going to test
       EmployeeDto saveEmployee = employeeService.saveEmployee(employeeDto);

      //then - verify the output

        assertThat(saveEmployee).isNotNull();

    }

    @DisplayName("Test for save employee method Which throw exceptions")
    @Test
    public void givenEmployeeExistEmail_whenSaveEmployee_thenThrowException (){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employeeDto.getEmail())).willReturn(Optional.of(employee));

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        //when - action or behaviour that we are going to test
        assertThrows(ResourceNotFoundException.class,()->{
           employeeService.saveEmployee(employeeDto);
        });


        //then - verify the output

        verify(employeeRepository, never()).save(any(Employee.class));

    }
    @DisplayName("Junit test for getting all employee list")
    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnListOfEmployee (){
      //given - precondition or setup
       Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Michael")
                .lastName("John")
                .email("MichaelJohn@gmail.com")
                .build();


        given(employeeRepository.findAll()).willReturn(List.of(employee1,employee));

      //when - action or behaviour that we are going to test
        List<EmployeeDto> employeeList = employeeService.getAllEmployee();

      //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList).hasSize(2);


    }
    @DisplayName("Test for getting all  employee list ")
    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnEmptyList (){
      //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Michael")
                .lastName("John")
                .email("MichaelJohn@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or behaviour that we are going to test
       List<EmployeeDto> employeeList = employeeService.getAllEmployee();

        //then - verify the output
        assertThat(employeeList.size()).isEqualTo(0);
        assertThat(employeeList).isEmpty();

    }

    @DisplayName("Test to find employeeId")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject (){
      //given - precondition or setup
      given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

      //when - action or behaviour that we are going to test
        Optional<EmployeeDto> optionalEmployee = employeeService.findEmployeeById(employee.getId());

      //then - verify the output
        assertThat(optionalEmployee).isNotNull();
        assertThat(optionalEmployee.isPresent()).isTrue();
        assertThat(optionalEmployee.stream().count()).isOne();

    }

    @DisplayName("Test for updating employee details")
    @Test
    public void givenEmployeeIdAndObject_whenUpdateEmployee_thenReturnEmployee (){
      //given - precondition or setup
      given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        Optional<EmployeeDto> optionalEmployee = employeeService.findEmployeeById(employee.getId());
        EmployeeDto currentEmployee = optionalEmployee.get();
        currentEmployee.setFirstName("Mike");
        currentEmployee.setLastName("Peter");
      given(employeeRepository.save(any(Employee.class))).willReturn(employee);

      //when - action or behaviour that we are going to test
       EmployeeDto updatedEmpolyee = employeeService.updateEmployee(employeeDto.getId(), currentEmployee);

      //then - verify the output
        assertThat(optionalEmployee.isPresent()).isTrue();
        assertThat(updatedEmpolyee).isNotNull();
        assertThat(updatedEmpolyee.getFirstName()).isEqualTo("Mike");
        assertThat(updatedEmpolyee.getLastName()).isEqualTo("Peter");


    }
    @Test
    public void givenEmployeeId_whenDeleteEmployeeById_thenReturn (){
      //given - precondition or setup
        long id =1L;
        given(employeeRepository.findById(id)).willReturn(Optional.of(employee));
        Optional<EmployeeDto> optionalEmployee = employeeService.findEmployeeById(employeeDto.getId());
        EmployeeDto currentEmployee = optionalEmployee.get();
        willDoNothing().given(employeeRepository).deleteById(currentEmployee.getId());

      //when - action or behaviour that we are going to test
        employeeService.deleteEmployee(employee.getId());

      //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employee.getId());

    }



}
