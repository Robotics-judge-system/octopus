package ru.anarcom.octopus.exceptions

import org.springframework.security.authentication.BadCredentialsException

class IncorrectPasswordException(
    message:String
):BadCredentialsException(message)
