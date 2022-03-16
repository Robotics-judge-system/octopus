package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class ValidationException(message: String) : BaseException(message, HttpStatus.CONFLICT)
