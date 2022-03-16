package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(message: String) : BaseException(message, HttpStatus.NOT_FOUND)
