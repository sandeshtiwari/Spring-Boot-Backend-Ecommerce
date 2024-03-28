package com.backend.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.FileHandlingDto;

public interface FileUpload {
    // String uploadFile(MultipartFile multipartFile) throws IOException;
    FileHandlingDto uploadFile(MultipartFile multipartFile) throws IOException;
}
