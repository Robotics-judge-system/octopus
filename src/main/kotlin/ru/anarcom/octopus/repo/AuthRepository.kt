package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.Status

interface AuthRepository: JpaRepository<Auth, Long> {
    fun findByRefreshTokenAndStatus(token:String, status: Status):Auth
    fun existsByRefreshToken(token: String):Boolean
}