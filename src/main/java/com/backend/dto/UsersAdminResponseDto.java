package com.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersAdminResponseDto {
    private List<UserResponseDto> users;
    private long totalNumberOfUsers;
}
