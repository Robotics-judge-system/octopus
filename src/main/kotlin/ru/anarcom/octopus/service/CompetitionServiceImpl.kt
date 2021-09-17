package ru.anarcom.octopus.service

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repo.CompetitonRepository
import java.time.Instant

@Service
class CompetitionServiceImpl(
    private val competitionRepository: CompetitonRepository
):CompetitionService {
    override fun delete(competition: Competition) {
        competition.status = Status.DELETED
        competitionRepository.save(competition)
    }

    override fun create(user: User, name: String, dateFrom: Instant?, dateTo: Instant?): Competition {
        TODO("Not yet implemented")
    }

    override fun findByUser(user: User): List<Competition> {
        TODO("Not yet implemented")
    }
}