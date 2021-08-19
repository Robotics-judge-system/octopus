package ru.anarcom.octopus.security

import org.springframework.beans.factory.annotation.Value

class JwtTokenProvider {
    @Value("\${jwt.token.secret}")
    private lateinit var secret:String

    @Value("\${jwt.token.expired}")
    private var validInHours: Long = 1
}