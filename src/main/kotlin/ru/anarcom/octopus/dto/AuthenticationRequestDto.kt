package ru.anarcom.octopus.dto;

import lombok.Data;

/**
 * DTO with information for Login.
 */
@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
}
