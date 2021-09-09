package ru.anarcom.octopus.service

import javassist.NotFoundException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repo.AuthRepository
import java.time.Clock

@Service
class AuthServiceImpl(
    private val authRepository: AuthRepository,
    private val clock: Clock
) : AuthService {
    override fun getNewRefreshTokenForUser(user: User): String {
        var refreshToken: String

        do {
            refreshToken = RandomStringUtils.random(20, true, true)
        } while (authRepository.existsByRefreshToken(refreshToken))

        val auth = Auth(
            refreshToken = refreshToken,
            user = user
        )

        auth.created = clock.instant()
        auth.updated = clock.instant()
        auth.status = Status.ACTIVE
        authRepository.save(auth)
        return refreshToken
    }

    override fun getUserByRefreshToken(token: String): User {
        if (!authRepository.existsByRefreshToken(token)){
            throw NotFoundException("User with that refresh token not found")
        }
        val auth = authRepository.findByRefreshTokenAndStatus(token, Status.ACTIVE)
        auth.updated = clock.instant()
        authRepository.save(auth)
        return auth.user!!
    }
}