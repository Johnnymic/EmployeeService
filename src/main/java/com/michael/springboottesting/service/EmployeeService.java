package com.michael.springboottesting.service;

import com.michael.springboottesting.EmployeeDto.ApiResponse;
import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import com.michael.springboottesting.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeDto saveEmployee (EmployeeDto employee);

   List<EmployeeDto> getAllEmployee();

    Optional<EmployeeDto> findEmployeeById(Long id);

    EmployeeDto updateEmployee(Long id, EmployeeDto employee);
    void deleteEmployee(Long id);

    ApiResponse uploadFile(MultipartFile file) throws Exception;

    List<ApiResponse> uploadMultipleFiles(MultipartFile[] files) throws Exception;

    List<ApiResponse> getAllFile();
}
