package ru.anarcom.octopus.security

import javax.security.sasl.AuthenticationException

class JwtAuthException(details: String): AuthenticationException(details)