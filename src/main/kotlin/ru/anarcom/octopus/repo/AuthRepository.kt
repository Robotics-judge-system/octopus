package ru.anarcom.octopus.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import java.time.Instant

interface AuthRepository: JpaRepository<Auth, Long> {
    fun findByRefreshTokenAndStatus(token: String, status: Status): Auth?

    fun existsByRefreshToken(token: String): Boolean

    fun findByIdAndUser(token: Long, user: User): Auth?

    fun findAllByUser(user: User): List<Auth>

    fun findAllByUserAndStatus(user: User, status: Status): List<Auth>

    fun findAllByUpdatedBeforeAndStatusNot(
        lastUpdated: Instant,
        pageData: Pageable,
        status: Status
    ): Page<Auth>
}
