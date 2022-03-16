package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class UpdateAttemptResultException(
    message: String
) : BaseException(message, HttpStatus.CONFLICT)
