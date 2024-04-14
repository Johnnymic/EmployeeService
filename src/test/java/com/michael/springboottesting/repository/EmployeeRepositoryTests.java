package com.michael.springboottesting.repository;

import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import com.michael.springboottesting.enums.EmployeeStatus;
import com.michael.springboottesting.enums.Gender;
import com.michael.springboottesting.model.Address;
import com.michael.springboottesting.model.Employee;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private  EmployeeRepository employeeRepository;

    private Employee employee;

    private EmployeeDto employeeDto;

    @BeforeEach
    public void setUp()
    {
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

    }

    //JUnit test for save employee operation
    @DisplayName("Test to save employee operator")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSaveEmployee(){
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        Employee saveEmployee = employeeRepository.save(employee);

        //then - verify the output.

        assertThat(saveEmployee).isNotNull();
        assertThat(saveEmployee.getId()).isGreaterThan(0);
    }


   @Test
   @DisplayName("Test to find the List of  employees operator")
   public void givenEmployeeList_whenFindAll_thenEmployeeList (){
     //given - precondition or setup
       EmployeeDto employeeDto2 = EmployeeDto.builder()
               .firstName("Mike")
               .lastName("Joe")
               .email("MichaelJohn@gmail.com")
               .build();
   Employee    employee2 = Employee.builder()
               .firstName(employeeDto2.getFirstName())
               .lastName(employeeDto2.getLastName())
               .email(employeeDto2.getEmail())
               .build();


       employeeRepository.save(employee);
       employeeRepository.save(employee2);

     //when - action or behaviour that we are going to test
       List<Employee> employeeList = employeeRepository.findAll();


     //then - verify the output
       assertThat(employeeList).isNotNull();
       assertThat(employeeList.size()).isEqualTo(2);
       assertThat(employeeList.stream().toList()).first();


   }

   @DisplayName("Test for get employee by id operator")
   @Test
   public void givenEmployeeid_whenFindById_thenReturnEmployeeObject (){
     //given - precondition or setup
       employeeRepository.save(employee);


     //when - action or behaviour that we are going to test
    Optional<Employee>  findOneEmployee = employeeRepository.findById(employee.getId());

    //then - verify the output
       assertThat(findOneEmployee.isPresent()).isTrue();
       assertThat(findOneEmployee.get()).isNotNull();

   }

 @DisplayName("Test for employee by email operation")
   @Test
   public void givenEmployeeEmail_whenFindByEmail_thenEmployee(){
     //given - precondition or setup
       employeeRepository.save(employee);
     //when - action or behaviour that we are going to test
      Optional<Employee> employeeEmail = employeeRepository.findByEmail(employee.getEmail());

     //then - verify the output
       assertThat(employeeEmail.isPresent()).isTrue();
       assertThat(employeeEmail.get()).isNotNull();
       assertThat(employeeEmail.stream().count()).isOne();

   }
  @DisplayName("Test for updating employee operation")
   @Test
   public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee (){
     //given - precondition or setup

       employeeRepository.save(employee);


     //when - action or behaviour that we are going to test
       Employee savedEmployee = employeeRepository.findById(employee.getId()).get();

       savedEmployee.setEmail("Johnny@gmail");
       savedEmployee.setFirstName("King");
       savedEmployee.setLastName("Authur");

       assertThat(savedEmployee.getEmail()).isEqualTo("Johnny@gmail");
       assertThat(savedEmployee.getFirstName()).isEqualTo("King");


     //then - verify the output

   }
   @DisplayName("Test for deleting employee")
   @Test
   public void givenEmployeeObject_whenDelete_thenRemoveEmployeeObject (){
     //given - precondition or setup
       employeeRepository.save(employee);


     //when - action or behaviour that we are going to test
       employeeRepository.delete(employee);
       Optional<Employee> employee1 = employeeRepository.findById(employee.getId());

     //then - verify the output
       assertThat(employee1.isPresent()).isFalse();
       assertThat(employee1.isEmpty()).isTrue();

   }
@DisplayName("Test for customer query using JPQL index")
   @Test
   public void givenFirstAndLastName_whenFindByJPQL_thenReturnEmployeObject (){
     //given - precondition or setup
       employeeRepository.save(employee);

       String lastName = "John";

       String firstName = "Michael";

     //when - action or behaviour that we are going to test
    Employee employee1 = employeeRepository.findByJPQL(firstName, lastName);

    //then - verify the output
    assertThat(employee1).isNotNull();

    }

    @DisplayName("Test for customer query using Native query index")
    @Test
    public void givenFirstAndLastName_whenFindByNativeQuery_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee employee1 = employeeRepository.findByNativeNameQuery(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(employee1).isNotNull();
        assertThat(employee1.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employee1.getLastName()).isEqualTo(employee.getLastName());
        assertThat(employee1.getEmail()).isEqualTo(employee.getEmail());
        // Add additional assertions based on your requirements
    }

    @DisplayName("Test for paginated list of employee")
    @Test
    public void  givenPageAndSize_whenFindPaginatedEmployees_thenReturnPageOfEmployees(){
      //given - precondition or setup
    Employee employee2 = Employee.builder()
            .firstName("Mike")
            .lastName("Joe")
            .email("MichaelJohn@gmail.com")
            .build();
    employeeRepository.save(employee);
    employeeRepository.save(employee2);
    int page =0;
    int size =1;

      //when - action or behaviour that we are going to test
    Pageable pageable = PageRequest.of(page,size);
    Page<Employee> employeePage= employeeRepository.findByPageAndSize(pageable);


      //then - verify the output
    assertThat(employeePage).isNotNull();
    assertThat(employeePage.getContent()).hasSize(size);
    assertThat(employeePage).contains(employee);
    assertThat(employeePage).doesNotContain(employee2);

    }


}
