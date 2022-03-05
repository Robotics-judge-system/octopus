package ru.anarcom.octopus.service.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.AttemptResult
import ru.anarcom.octopus.repo.AttemptResultRepository
import ru.anarcom.octopus.service.AttemptResultService
import java.time.Clock

@Service
class AttemptResultServiceImpl(
    private val attemptResultRepository: AttemptResultRepository,
    private val clock: Clock,
):AttemptResultService {
    override fun save(attempt: AttemptResult): AttemptResult {
        attempt.updated = clock.instant()
        return attemptResultRepository.save(attempt)
    }

    override fun saveNew(attempt: AttemptResult): AttemptResult {
        attempt.created = clock.instant()
        return save(attempt)
    }
}
