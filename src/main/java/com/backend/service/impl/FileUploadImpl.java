package com.backend.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.FileHandlingDto;
import com.backend.service.FileUpload;
import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadImpl implements FileUpload {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public FileHandlingDto uploadFile(MultipartFile multipartFile) throws IOException {
        String secureUrl = cloudinary.uploader()
                .upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString())).get("secure_url")
                .toString();
        FileHandlingDto fileHandlingDto = new FileHandlingDto(secureUrl);
        return fileHandlingDto;
    }

}
