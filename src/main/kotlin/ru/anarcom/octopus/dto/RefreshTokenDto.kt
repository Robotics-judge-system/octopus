package ru.anarcom.octopus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = false)
data class RefreshTokenDto(
    val refresh:String
)
