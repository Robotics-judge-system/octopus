package ru.anarcom.octopus.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginResponseDto(
    val username: String,

    val token: String,

    @get:JsonProperty("refresh_token")
    val refreshToken: String,

    @get:JsonProperty("expires_after_sec")
    val expiresAfterSec: Long,
)