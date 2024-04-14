package com.michael.springboottesting.EmployeeDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

    private String fileName;

    private String downloadedUrl;

    private String fileType;

    private Long fileSize;
}
