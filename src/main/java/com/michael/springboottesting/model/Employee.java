package com.michael.springboottesting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.michael.springboottesting.enums.EmployeeStatus;
import com.michael.springboottesting.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column( name = "email",nullable = false)
    private String email;


    @Column( name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = {"employee"} , allowGetters = true)
    private List<Address> address;

    @Column(name = "employee_status")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;


}
