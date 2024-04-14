package com.michael.springboottesting.repository;

import com.michael.springboottesting.model.EmployeeFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<EmployeeFiles, Long> {


}
