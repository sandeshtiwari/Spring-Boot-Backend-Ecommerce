package com.backend.service;

import com.backend.dto.UserRequestDto;
import com.backend.dto.UserResponseDto;

public interface UserService {

    public UserResponseDto editUserDetails(UserRequestDto userRequestDto);

}
