package ru.anarcom.octopus.service

import javassist.NotFoundException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repo.AuthRepository
import ru.anarcom.octopus.util.logger
import java.time.Clock

@Service
class AuthServiceImpl(
    private val authRepository: AuthRepository,
    private val clock: Clock
) : AuthService {

    private val log = logger()

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
            log.warn("getted not existed refresh token (token)")
            throw NotFoundException("User with that refresh token not found")
        }
        var auth = authRepository.findByRefreshTokenAndStatus(token, Status.ACTIVE)
        auth.updated = clock.instant()
        auth = authRepository.save(auth)
        return auth.user!!
    }
}
