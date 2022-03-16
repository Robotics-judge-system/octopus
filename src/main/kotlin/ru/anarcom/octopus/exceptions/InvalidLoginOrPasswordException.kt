package ru.anarcom.octopus.exceptions

import org.springframework.http.HttpStatus

class InvalidLoginOrPasswordException
    : BaseException("invalid username or password", HttpStatus.FORBIDDEN)
