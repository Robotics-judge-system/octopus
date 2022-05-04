package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class CalculationException(
    override val message: String
) : BaseException(message, HttpStatus.INTERNAL_SERVER_ERROR)