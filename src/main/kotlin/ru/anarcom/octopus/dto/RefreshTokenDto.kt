package ru.anarcom.octopus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Dto with information about refresh token.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
data class RefreshTokenDto(
    val refresh:String
)
