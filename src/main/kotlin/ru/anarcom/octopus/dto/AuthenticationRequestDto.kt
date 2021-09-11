package ru.anarcom.octopus.dto

/**
 * DTO with information for Login.
 */
data class AuthenticationRequestDto(
    val username: String = "",
    val password: String = "",
)