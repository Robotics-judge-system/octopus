package ru.anarcom.octopus.service.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.exceptions.NotFoundException
import ru.anarcom.octopus.repo.AttemptRepository
import ru.anarcom.octopus.service.AttemptService
import java.time.Clock

@Service
class AttemptServiceImpl(
    private val attemptRepository: AttemptRepository,
    private val clock: Clock
): AttemptService {
    override fun save(attempt: Attempt): Attempt {
        attempt.updated = clock.instant()
        return attemptRepository.save(attempt)
    }

    override fun saveNew(attempt: Attempt): Attempt {
        attempt.created = clock.instant()
        return save(attempt)
    }

    override fun findAttemptByCategoryAndIdOrThrow(category: Category, id: Long): Attempt =
        attemptRepository.findOneByCategoryAndId(category, id)
            ?: throw NotFoundException("No such attempt")

    override fun isFormulaProtocolCanBeDeleted(formulaProtocol: FormulaProtocol): Boolean =
        attemptRepository.countAllByFormulaProtocolAndStatusNot(
            formulaProtocol,
            Status.DELETED
        ) == 0L

}
