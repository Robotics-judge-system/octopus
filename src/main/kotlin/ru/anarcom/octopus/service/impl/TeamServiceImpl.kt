package ru.anarcom.octopus.service.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Participant
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.Team
import ru.anarcom.octopus.exceptions.NotFoundException
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

    override fun getByCategoryAndId(category: Category, id: Long): Team =
        teamRepository.getByIdAndCategory(id, category)

    override fun getAllByCategory(category: Category): List<Team> =
        teamRepository.findAllByCategory(category)

    override fun getAllActiveByCategory(category: Category): List<Team> =
        teamRepository.findAllByCategoryAndStatusNot(category, Status.DELETED)

    override fun getByIdAndCategory(id: Long, category: Category) =
        teamRepository.getByIdAndCategory(id, category)

    override fun deleteByIdAndCategory(id: Long, category: Category): Team {
        val team = teamRepository.getByCategoryAndId(category, id)
        if (team.status == Status.DELETED)
            return team
        team.status = Status.DELETED
        return save(team)
    }

    override fun renameTeam(id: Long, category: Category, newName: String): Team {
        val team = teamRepository.getByIdAndCategory(id, category)
        if (team.status == Status.DELETED || team.teamName == newName)
            return team
        team.teamName = newName
        return save(team)
    }

    override fun getByIdAndCategoryOrThrow(id: Long, category: Category): Team {
        return teamRepository.findByCategoryAndId(category, id)
            ?: throw NotFoundException("team for that data not found")
    }
}
