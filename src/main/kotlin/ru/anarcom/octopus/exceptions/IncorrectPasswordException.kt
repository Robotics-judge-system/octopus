package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class IncorrectPasswordException(
    message: String
) : BaseException(message, HttpStatus.FORBIDDEN)
