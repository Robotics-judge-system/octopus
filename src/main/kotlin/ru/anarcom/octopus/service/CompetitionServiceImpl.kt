package ru.anarcom.octopus.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repo.CompetitonRepository
import ru.anarcom.octopus.util.TestClock
import ru.anarcom.octopus.util.logger
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@Service
class CompetitionServiceImpl(
    private val competitionRepository: CompetitonRepository,
    private val clock: Clock
):CompetitionService {
    private val log = logger()

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun delete(competition: Competition) {
        competition.status = Status.DELETED
        save(competition)
    }

    override fun create(
        user: User,
        name: String,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Competition =
        competitionRepository.save(
            Competition(
                user = user,
                name = name,
                dateFrom = dateFrom,
                dateTo = dateTo,
            ).apply {
                status = Status.ACTIVE
                created = clock.instant()
                updated = clock.instant()
            }
        )

    override fun findByUser(user: User): List<Competition> =
        competitionRepository.findAllByUser(user)

    override fun hardDelete(competition: Competition) {
        TODO("Not yet implemented")
    }

    override fun hardDeleteById(id: Long) {
        TODO("Not yet implemented")
    }

    private fun save(competition: Competition): Competition =
        competitionRepository.save(
            competition.apply {
                updated = clock.instant()
            }
        )
}
