package ru.anarcom.octopus.jobs.auth

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Auth
import ru.anarcom.octopus.service.AuthService
import java.time.Clock
import java.time.temporal.ChronoUnit

@Service
class AuthInvalidationJob(
    private val clock: Clock,
    private val authService: AuthService,
) {
    fun invalidate(invalidateAfterDays: Long){
        val limit = clock.instant().minus(invalidateAfterDays, ChronoUnit.DAYS)
        val t: Pageable = PageRequest.of(0, 10)
        var page: List<Auth>
        var counter = 0
        do {
            page = authService.getAllAuthsForInstanceBefore(limit, t)
            page.forEach {
                authService.invalidateRefreshTokenById(it.user, it.id)
            }
            counter++
        } while (page.isNotEmpty() && counter < 100)
    }
}
