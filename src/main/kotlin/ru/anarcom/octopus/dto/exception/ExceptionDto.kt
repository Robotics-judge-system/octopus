package ru.anarcom.octopus.dto.exception

import com.fasterxml.jackson.annotation.JsonProperty

data class ExceptionDto(
    @field:JsonProperty("exception_message")
    val exceptionMessage: String,
)
