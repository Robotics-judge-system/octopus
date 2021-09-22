package ru.anarcom.octopus.service

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repo.CompetitonRepository
import ru.anarcom.octopus.util.logger
import java.time.Clock
import java.time.Instant

@Service
class CompetitionServiceImpl(
    private val competitionRepository: CompetitonRepository,
    private val clock: Clock
):CompetitionService {
    private val log = logger()

    override fun deleteById(id: Long) =
        delete(competitionRepository.findById(id).orElseThrow())

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

    override fun hardDelete(competition: Competition) =
        competitionRepository.delete(competition
            .apply {
                log.info("Competition deleted from db $competition")
            }
        )

    //TODO refactor to .toString() method
    override fun hardDeleteById(id: Long) =
        competitionRepository.deleteById(id).apply {
            log.info("Competition deleted from db (by id = $id)")
        }

    private fun save(competition: Competition): Competition =
        competitionRepository.save(
            competition.apply {
                updated = clock.instant()
            }
        )
}
