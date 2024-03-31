package com.backend.service;

import java.util.Map;

import com.backend.dto.UserRequestDto;
import com.backend.dto.UserResponseDto;

public interface UserService {

    public Map<String, String> editUserDetails(UserRequestDto userRequestDto);

}
