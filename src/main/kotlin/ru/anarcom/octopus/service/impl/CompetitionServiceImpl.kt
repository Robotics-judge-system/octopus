package ru.anarcom.octopus.service.impl

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repo.CompetitionRepository
import ru.anarcom.octopus.service.CompetitionService
import ru.anarcom.octopus.util.logger
import java.time.Clock
import java.time.Instant

@Service
class CompetitionServiceImpl(
    private val competitionRepository: CompetitionRepository,
    private val clock: Clock
): CompetitionService {
    private val log = logger()

    override fun deleteById(id: Long):Competition =
        delete(competitionRepository.findById(id).orElseThrow())

    override fun delete(competition: Competition):Competition {
        competition.status = Status.DELETED
        return save(competition)
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

    override fun findAllActiveByUser(user: User): List<Competition> =
        competitionRepository.findAllByUserAndStatus(user, Status.ACTIVE)

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

    override fun rename(user: User, id: Long, name: String): Competition {
        val competition = competitionRepository.getById(id)
        // !ABAC!
        if(user.id != competition.id){
            throw UsernameNotFoundException("This is not competition of current user")
        }
        competition.name = name
        competition.updated = clock.instant()
        return competitionRepository.save(competition)
    }

    override fun getById(id: Long): Competition =
        competitionRepository.getById(id)

    override fun deleteByIdAndUser(user: User, id: Long): Competition {
        val competition = competitionRepository.getById(id)
        // !ABAC!
        if(competition.getUserOrThrow().id != user.id){
            throw UsernameNotFoundException("This is not competition of current user")
        }
        competition.status = Status.DELETED
        competition.updated = clock.instant()
        return competitionRepository.save(competition)
    }

    private fun save(competition: Competition): Competition =
        competitionRepository.save(
            competition.apply {
                updated = clock.instant()
            }
        )
}
