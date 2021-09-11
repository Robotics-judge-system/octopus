package ru.anarcom.octopus.dto.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChangeUserInfoDto(
    val name:String? = null
)
