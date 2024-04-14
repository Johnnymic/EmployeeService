package com.michael.springboottesting.repository;

import com.michael.springboottesting.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName,String lastName);

    @Query("select emp from Employee emp where emp.firstName=:firstName and emp.lastName=:lastName")
    Employee findByJPQLNameParams(String firstName, String lastName);


    @Query(value = "select * from employees emp where emp.first_name=:firstName and emp.last_name=:lastName", nativeQuery = true)
    Employee findByNativeNameQuery(@Param("firstName") String firstName,@Param("lastName") String lastName);


    @Query(value = "select e from Employee e ", countQuery = "select  count(e) from Employee  e")
    Page<Employee> findByPageAndSize(Pageable pageable);
}
