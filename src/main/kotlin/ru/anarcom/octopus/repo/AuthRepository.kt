package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User

interface AuthRepository: JpaRepository<Auth, Long> {
    fun findByRefreshTokenAndStatus(token: String, status: Status): Auth
    fun existsByRefreshToken(token: String): Boolean
    fun findByIdAndUser(token: Long, user: User): Auth?
    fun findAllByUser(user: User): List<Auth>
}