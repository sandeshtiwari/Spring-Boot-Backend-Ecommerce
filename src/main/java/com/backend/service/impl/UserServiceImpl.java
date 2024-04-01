package com.backend.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.dto.OrderAdminResponseDto;
import com.backend.dto.OrderResponseDto;
import com.backend.dto.UserRequestDto;
import com.backend.dto.UserResponseDto;
import com.backend.dto.UsersAdminResponseDto;
import com.backend.exceptions.UserNotFoundException;
import com.backend.model.OrderEntity;
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

    @Override
    public UsersAdminResponseDto getAllUserssAdmin(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserEntity> allUsers = userRepository.findAll(pageable);
        List<UserEntity> listOfOrders = allUsers.getContent();
        List<UserResponseDto> allUsersResponseDto = listOfOrders.stream().map(p -> mapToUserResponseDto(p))
                .collect(Collectors.toList());

        UsersAdminResponseDto usersAdminResponseDto = new UsersAdminResponseDto(allUsersResponseDto,
                userRepository.count());
        return usersAdminResponseDto;
    }

    @Override
    public UserEntity getByUsername(String username) {
        // return userRepository.findById(userId).orElseThrow(() -> new
        // UserNotFoundException("User not found!"));
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

}
