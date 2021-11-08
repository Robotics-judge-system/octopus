package ru.anarcom.octopus.service.impl

import javassist.NotFoundException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repo.AuthRepository
import ru.anarcom.octopus.service.AuthService
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
        if (!authRepository.existsByRefreshToken(token)) {
            log.warn("getted not existed refresh token (token)")
            throw NotFoundException("User with that refresh token not found")
        }
        var auth = authRepository.findByRefreshTokenAndStatus(token, Status.ACTIVE)
        auth.updated = clock.instant()
        auth = authRepository.save(auth)
        return auth.user!!
    }

    @Transactional
    override fun invalidateRefreshTokenById(user: User, id: Long): Auth {
        val token = authRepository.findByIdAndUser(id, user)
        if (token == null) {
            log.warn("person try to delete not user's token (token id=$id, userid=${user.id}")
            throw NotFoundException("Invalid user or token id")
        }
        token.status = Status.DELETED
        return save(token)
    }

    override fun getAuthsForUser(user: User): List<Auth> =
        authRepository.findAllByUser(user)

    private fun save(auth: Auth): Auth {
        auth.updated = clock.instant()
        return authRepository.save(auth)
    }
}
