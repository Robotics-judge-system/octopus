package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class UpdateAttemptException(override val message: String) :
    BaseException(message = message, httpStatus = HttpStatus.CONFLICT)
