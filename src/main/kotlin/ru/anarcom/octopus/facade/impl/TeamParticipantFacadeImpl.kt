package ru.anarcom.octopus.facade.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.anarcom.octopus.dto.team.ParticipantDto
import ru.anarcom.octopus.entity.*
import ru.anarcom.octopus.facade.TeamParticipantFacade
import ru.anarcom.octopus.service.ParticipantService
import ru.anarcom.octopus.service.TeamService


@Service
class TeamParticipantFacadeImpl(
    val teamService: TeamService,
    val participantService: ParticipantService
) : TeamParticipantFacade {
    @Transactional
    override fun registerTeam(
        category: Category,
        teamName: String,
        participantDtos: MutableList<ParticipantDto>
    ): Team {
        val participants = mutableListOf<Participant>()
        val t = teamService.addTeam(category, teamName, participants)
        participantDtos.forEach{
                participantService.add(
                    Participant(
                        team = t,
                        name = it.name,
                        teamRole = ParticipantRole.valueOf(it.teamRole),
                        status = Status.ACTIVE
                    )
                )
        }
        return teamService.getByCategoryAndId(category, t.id).apply {
            this.participants = participantService.getByTeam(this).toMutableList()
        }
    }
}
