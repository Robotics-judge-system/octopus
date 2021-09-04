package ru.anarcom.octopus.service

import ru.anarcom.octopus.model.User

interface AuthService {
    fun getNewRefreshTokenForUser(user: User): String
    fun getUserByRefreshToken(token: String): User?
}