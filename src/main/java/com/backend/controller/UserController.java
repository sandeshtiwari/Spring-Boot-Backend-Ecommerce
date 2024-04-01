package com.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.OrderResponseDto;
import com.backend.dto.UserRequestDto;
import com.backend.service.impl.OrderServiceImpl;
import com.backend.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @PutMapping("/edit")
    public Map<String, String> getUserDetails(@RequestBody UserRequestDto userRequestDto) {
        return userServiceImpl.editUserDetails(userRequestDto);
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> getAllUserOrders(@RequestParam String username) {
        return orderServiceImpl.getAllUserOrders(username);
    }

}
