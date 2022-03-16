package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class CannotActivateException(message: String) :
        BaseException(message, HttpStatus.CONFLICT)
