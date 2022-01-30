package ru.anarcom.octopus.service.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Participant
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.Team
import ru.anarcom.octopus.repo.TeamRepository
import ru.anarcom.octopus.service.TeamService
import java.time.Clock

@Service
class TeamServiceImpl(
    private val teamRepository: TeamRepository,
    private val clock: Clock
) : TeamService {
    private fun save(team: Team):Team {
        team.updated = clock.instant()
        return teamRepository.save(team)
    }

    override fun addTeam(
        category: Category,
        teamName: String,
        participants: MutableList<Participant>
    ): Team {
        return save(
            Team(
                category = category,
                teamName = teamName,
                status = Status.ACTIVE,
                created = clock.instant(),
                participants = mutableListOf()
            )
        )
    }

    override fun getByCategoryAndId(category: Category, id: Long): Team {
        return teamRepository.getByIdAndCategory(id, category)
    }


}