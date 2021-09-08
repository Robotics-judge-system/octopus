package ru.anarcom.octopus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = false)
data class RegistrationUserDto(
    val username:String,
    val email:String,
    val name:String,
    val password:String,
)