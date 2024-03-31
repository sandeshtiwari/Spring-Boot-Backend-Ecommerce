package com.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.UserRequestDto;
import com.backend.service.impl.UserServiceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PutMapping("/edit")
    public Map<String, String> putMethodName(@RequestBody UserRequestDto userRequestDto) {
        return userServiceImpl.editUserDetails(userRequestDto);
    }
}
