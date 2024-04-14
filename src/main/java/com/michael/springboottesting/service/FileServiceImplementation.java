package com.michael.springboottesting.service;

import com.michael.springboottesting.model.Employee;
import com.michael.springboottesting.model.EmployeeFiles;
import com.michael.springboottesting.repository.EmployeeRepository;
import com.michael.springboottesting.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class FileServiceImplementation  implements  FileService{

    private FileRepository fileRepository;

    public FileServiceImplementation(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public EmployeeFiles saveAttachment(MultipartFile file) throws Exception {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")) {
                throw  new Exception("File contains invalid character " + fileName);
            }
            if(fileName.getBytes().length > (1024*1024)){
                throw  new Exception("File size exceeded maximum");
            }
            EmployeeFiles employeeFile = new EmployeeFiles(fileName,file.getContentType(),file.getBytes());
            return fileRepository.save(employeeFile);

        }catch(MaxUploadSizeExceededException e){
            throw new MaxUploadSizeExceededException(1);

        }catch (Exception e)
        {
            throw new Exception("Could not save file " + fileName);
        }

    }

    @Override
    public void saveFiles(MultipartFile[] files) {
        Arrays.asList(files).forEach(file -> {
            try{
                 saveAttachment(file);
            }catch(Exception e){
               throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<EmployeeFiles> getAllEmployee() {
        return fileRepository.findAll();
    }
}
