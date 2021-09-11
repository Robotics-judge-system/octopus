package ru.anarcom.octopus.dto.login

/**
 * DTO with information for Login.
 */
data class AuthenticationRequestDto(
    val username: String = "",
    val password: String = "",
)