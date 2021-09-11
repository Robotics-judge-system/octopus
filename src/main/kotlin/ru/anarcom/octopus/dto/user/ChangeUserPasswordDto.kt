package ru.anarcom.octopus.dto.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = false)
data class ChangeUserPasswordDto(
    @set:JsonProperty("old_password")
    var oldPassword: String,
    @set:JsonProperty("new_password")
    var newPassword: String,
)
