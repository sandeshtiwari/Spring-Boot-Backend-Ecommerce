package com.backend.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.dto.UserRequestDto;
import com.backend.dto.UserResponseDto;
import com.backend.exceptions.UserNotFoundException;
import com.backend.model.UserEntity;
import com.backend.repository.UserRepository;
import com.backend.service.JWTService;
import com.backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> editUserDetails(UserRequestDto userRequestDto) {
        String tokenUsername = jwtService.extractUsername(userRequestDto.getToken());
        if (!tokenUsername.equals(userRequestDto.getOldUsername())) {
            throw new UserNotFoundException("Invalid token or username. Unable to find user.");
        }
        UserEntity userEntity = userRepository.findByUsername(tokenUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setFirstName(userRequestDto.getFirstName());
        userEntity.setLastName(userRequestDto.getLastName());
        if (!userRequestDto.getNewPassword().equals("")) {
            userEntity.setPassword(passwordEncoder.encode(userRequestDto.getNewPassword()));
        }
        userEntity.setUsername(userRequestDto.getUsername());
        userRepository.save(userEntity);

        Map<String, String> response = new HashMap<String, String>();
        response.put("username", userEntity.getUsername());

        return response;
    }

    private UserResponseDto mapToUserResponseDto(UserEntity userEntity) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(userEntity.getEmail());
        userResponseDto.setFirstName(userEntity.getFirstName());
        userResponseDto.setLastName(userEntity.getLastName());
        userResponseDto.setUsername(userEntity.getUsername());

        return userResponseDto;
    }

}
