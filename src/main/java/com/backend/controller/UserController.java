package com.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.UserRequestDto;
import com.backend.dto.UserResponseDto;
import com.backend.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PutMapping("/edit")
    public UserResponseDto putMethodName(@RequestBody UserRequestDto userRequestDto) {
        return userServiceImpl.editUserDetails(userRequestDto);
    }
}
