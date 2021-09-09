package ru.anarcom.octopus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Dto with information for creating of new User.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
data class RegistrationUserDto(
    val username:String,
    val email:String,
    val name:String,
    val password:String,
)