package ru.anarcom.octopus.jobs.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.anarcom.octopus.util.logger
import java.time.Clock

@Component
class AuthInvalidationJobExecutor(
    private val clock: Clock,
    private val authInvalidationJob: AuthInvalidationJob,

    @Value("\${job.auth.invalidate_after_days}")
    private var invalidateAfterDays: Long,
) {
    val logger = logger()

    @Scheduled(cron = "0 0 0/1 * * ?")
    fun invalidateAuths() {
        logger.info("Auth invalidation work started at ${clock.instant()}")
        authInvalidationJob.invalidate(invalidateAfterDays)
        logger.info("Auth invalidation work finished at ${clock.instant()}")
    }
}
