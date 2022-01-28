package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.User

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
}
