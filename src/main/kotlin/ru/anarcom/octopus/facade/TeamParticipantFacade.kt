package ru.anarcom.octopus.facade

import ru.anarcom.octopus.dto.team.ParticipantDto
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Team

interface TeamParticipantFacade {
    fun registerTeam(
        category: Category,
        teamName: String,
        participantDtos: MutableList<ParticipantDto>
    ): Team

    fun getAllByCategory(
        category: Category,
    ): List<Team>

    fun getAllNotDeletedByCategory(
        category: Category
    ): List<Team>

    fun getOneByIdAndCategory(id: Long, category: Category): Team

    fun deleteTeam(id: Long, category: Category): Team

}
