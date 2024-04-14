package com.michael.springboottesting.controller;

import com.michael.springboottesting.EmployeeDto.ApiResponse;
import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import com.michael.springboottesting.config.EnqueueDequeService;
import com.michael.springboottesting.service.EmployeeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {


    public EmployeeService employeeService;

    public final EnqueueDequeService enqueueDequeService;


    public EmployeeController(EmployeeService employeeService, EnqueueDequeService enqueueDequeService) {
        this.employeeService = employeeService;
        this.enqueueDequeService = enqueueDequeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public EmployeeDto createEmployee (@RequestBody  EmployeeDto employee){
        return employeeService.saveEmployee(employee);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDto> getAllEmployee(){
       List<EmployeeDto> employeeDto = employeeService.getAllEmployee();
        return employeeDto;
    }

    @GetMapping("/employee/{id}")
    @Cacheable(value = "employee", key = "#id")
    public ResponseEntity<EmployeeDto> getEmployeebyId(@PathVariable("id") Long id){
        return employeeService.findEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
    @PutMapping("{id}")
    public EmployeeDto updateEmployee(@PathVariable("id") Long id,@RequestBody EmployeeDto employee){
        return employeeService.updateEmployee(id,employee);

    }

    @DeleteMapping("{id}")
    public void deleteEmployee(@PathVariable("id")Long id){
        employeeService.deleteEmployee(id);
    }



    @PostMapping("/upload/single/file")
    public ResponseEntity<ApiResponse> uploadFile (@RequestParam("file") MultipartFile file) throws Exception {

        ApiResponse apiResponse = employeeService.uploadFile(file);

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PostMapping("/upload/multiple/files")
    public  ResponseEntity<List<ApiResponse>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<ApiResponse> apiResponses = employeeService.uploadMultipleFiles(files);
        return new ResponseEntity<>(apiResponses, HttpStatus.OK);

    }

     @GetMapping("/get/all/file")
    public ResponseEntity<List<ApiResponse>> getAllFiles(){
        List<ApiResponse> apiResponses = employeeService.getAllFile();

        return new ResponseEntity<>(apiResponses, HttpStatus.OK);
    }

    @PostMapping("/publish")
    public ResponseEntity<EmployeeDto> publishMessage(@RequestBody EmployeeDto customer){
        enqueueDequeService.publishMessage(customer);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }







}
