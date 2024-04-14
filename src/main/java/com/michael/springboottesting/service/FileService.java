package com.michael.springboottesting.service;

import com.michael.springboottesting.model.Employee;
import com.michael.springboottesting.model.EmployeeFiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    EmployeeFiles saveAttachment(MultipartFile file) throws Exception;

     void saveFiles(MultipartFile [] files);

     List<EmployeeFiles> getAllEmployee();
}
