package ru.anarcom.octopus.service

import org.springframework.data.domain.Pageable
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.User
import java.time.Instant

/**
 * Service for refresh token logic.
 */
interface AuthService {
    /**
     * Creates refresh token for user.
     * @return refresh token (as string)
     */
    fun getNewRefreshTokenForUser(user: User): String

    /**
     * Finds user by refresh token.
     */
    fun getUserByRefreshToken(token: String): User

    /**
     * Invalidates refresh token by id and user info.
     */
    fun invalidateRefreshTokenById(user: User, id: Long): Auth

    fun getActiveAuthsForUser(user: User): List<Auth>

    fun getAllAuthsForInstanceBefore(limitTime: Instant, pageData: Pageable):List<Auth>
}
