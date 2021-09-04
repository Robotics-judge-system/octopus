package ru.anarcom.octopus.service

import javassist.NotFoundException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.model.Status
import ru.anarcom.octopus.model.User
import ru.anarcom.octopus.repo.AuthRepository
import java.util.*

@Service
class AuthServiceImpl(
    private val authRepository: AuthRepository
) : AuthService {
    override fun getNewRefreshTokenForUser(user: User): String {
        var refreshToken: String = ""

        do {
            refreshToken = RandomStringUtils.random(20, true, true)
        } while (authRepository.existsByRefreshToken(refreshToken))

        val auth = Auth(
            refreshToken = refreshToken,
            user = user
        )

        auth.created = Date()
        auth.updated = Date()
        auth.status = Status.ACTIVE
        authRepository.save(auth)
        return refreshToken
    }

    override fun getUserByRefreshToken(token: String): User {
        if (!authRepository.existsByRefreshToken(token)){
            throw NotFoundException("User with that refresh token not found")
        }
        val auth = authRepository.findByRefreshTokenAndStatus(token, Status.ACTIVE)
        auth.updated = Date()
        authRepository.save(auth)
        return auth.user!!
    }
}