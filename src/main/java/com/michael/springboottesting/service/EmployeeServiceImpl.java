package com.michael.springboottesting.service;

import com.michael.springboottesting.EmployeeDto.ApiResponse;
import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import com.michael.springboottesting.enums.Gender;
import com.michael.springboottesting.exceptions.ResourceNotFoundException;
import com.michael.springboottesting.model.Address;
import com.michael.springboottesting.model.Employee;
import com.michael.springboottesting.model.EmployeeFiles;
import com.michael.springboottesting.repository.EmployeeRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements  EmployeeService{

@Autowired
    private EmployeeRepository employeeRepository;
@Autowired
   private  FileServiceImplementation fileService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, FileServiceImplementation fileService) {
        this.employeeRepository = employeeRepository;
        this.fileService = fileService;
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exist with the given email" + employeeRepository);
        }

       String newFormatted = formattedPhone(employee.getPhone());
        employee.setPhone(newFormatted);
        Employee newEmployee = new Employee();

        List<Address> addresses = new ArrayList<>();
        addresses.stream().map(address -> {
            Address address1 = new Address();
            address1.setStreet(employee.getStreet());
            address1.setCity(employee.getCity());
            address1.setState(employee.getState());
            return  address1;

        }).collect(Collectors.toList());




        newEmployee.setLastName(employee.getLastName());
        newEmployee.setEmail(employee.getEmail());
        newEmployee.setFirstName(employee.getFirstName());
        newEmployee.setGender(Gender.valueOf(employee.getGender()));
        newEmployee.setPhoneNumber(employee.getPhone());
        newEmployee.setAddress(addresses);
        Employee saveEmp= employeeRepository.save(newEmployee);
     //   return new EmployeeDto(saveEmp.getId(), saveEmp.getLastName(), saveEmp.getFirstName(), saveEmp.getEmail(), saveEmp.getGender().name(), saveEmp.getPhoneNumber());

    }

    private String  formattedPhone(String phone) {
        if(StringUtils.isEmpty(phone)){
            throw new  RuntimeException("PhoneNumber not found %s" + phone);
        }
        if(phone.contains("0"))
        {
         return "+234" + phone.trim().substring(1)  ;
        }


        return  phone;


    }

    @Override
    public List<EmployeeDto> getAllEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeDto>employeeDtoList = new ArrayList<>();
        employeeList.forEach( employee -> {
           EmployeeDto employeeDto = new EmployeeDto();
           employeeDto.setEmail(employee.getEmail());
           employeeDto.setFirstName(employee.getFirstName());
           employeeDto.setLastName(employee.getLastName());
           employeeDto.setLastName(employee.getFirstName());
           employeeDto.setPhone(employee.getPhoneNumber());
           employeeDto.setId(employee.getId());
           employeeDto.setGender(String.valueOf(employee.getGender()));
           employeeDtoList.add(employeeDto);
        });

        return employeeDtoList;
    }

    @Override
    public Optional<EmployeeDto> findEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()){
           throw  new ResourceNotFoundException("employee id is not found :" + employee.get().getId());
        }
        Employee isPresent = employee.get();
        EmployeeDto dto = new EmployeeDto();
        dto.setEmail(isPresent.getEmail());
        dto.setLastName(isPresent.getLastName());
        dto.setFirstName(isPresent.getFirstName());
        dto.setId(isPresent.getId());
        return Optional.of(dto);


    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            Employee currentEmployee = optionalEmployee.get();
            currentEmployee.setLastName(employee.getLastName());
            currentEmployee.setFirstName(employee.getFirstName());
           Employee saveEmployee = employeeRepository.save(currentEmployee);
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setEmail(saveEmployee.getEmail());
            employeeDto.setFirstName(saveEmployee.getFirstName());
            employeeDto.setLastName(saveEmployee.getLastName());
            return employeeDto;
        }
        throw new ResourceNotFoundException("Id does not exist :" + optionalEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
     Optional<Employee> deleteEmployee = employeeRepository.findById(id);
     if(!deleteEmployee.isPresent())
     {
         throw new ResourceNotFoundException("id not present :" + deleteEmployee.get());
     }

        employeeRepository.deleteById(deleteEmployee.get().getId());
    }

    @Override
    public ApiResponse uploadFile(MultipartFile file) throws Exception {
        EmployeeFiles attachment = fileService.saveAttachment(file);
        String downloadUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();
        return new ApiResponse(attachment.getFileName(),downloadUrl, file.getContentType(), file.getSize());
    }

    @Override
    public List<ApiResponse> uploadMultipleFiles(MultipartFile[] files) throws Exception {
        List<ApiResponse> responseList = new ArrayList<>();

        for (MultipartFile file : files){
            EmployeeFiles attachment = fileService.saveAttachment(file);
            String downloadUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/download/")
                    .path(attachment.getId())
                    .toUriString();
            ApiResponse apiResponse = new ApiResponse(attachment.getFileName(),downloadUrl, file.getContentType(), file.getSize());
            responseList.add(apiResponse);
        }
        return responseList;
    }

    @Override
    public List<ApiResponse> getAllFile() {
       List<EmployeeFiles>     employeeFiles   = fileService.getAllEmployee();
       List<ApiResponse> apiResponses = employeeFiles.stream().map(employeeFile -> {
                  String downLoadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                          .path("/download/")
                          .path(employeeFile.getId())
                          .toUriString();
                   ApiResponse apiResponse = new ApiResponse(employeeFile.getFileName(), downLoadUrl, employeeFile.getFileType(), (long) employeeFile.getData().length);
                   return  apiResponse;
               }).collect(Collectors.toList());
        return apiResponses;
    }


}
