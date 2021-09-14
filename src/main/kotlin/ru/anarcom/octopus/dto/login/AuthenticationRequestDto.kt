package ru.anarcom.octopus.dto.login

/**
 * DTO with information for Login.
 */
data class AuthenticationRequestDto(
    val login: String = "",
    val password: String = "",
)