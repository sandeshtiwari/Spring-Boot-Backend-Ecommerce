package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String oldUsername;
    private String email;
    private String firstName;
    private String lastName;
    private String newPassword;
    private String token;
}
