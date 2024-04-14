package com.michael.springboottesting.EmployeeDto;

import com.michael.springboottesting.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {

    private long id;

    private String lastName;

    private String firstName;

    private String email;

    private String phone;

    private String gender;

    private String street;

    private String state;

    private String city;

    private String date;



}
