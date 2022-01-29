package ru.anarcom.octopus.dto.team

import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.Team
import java.time.Clock
import java.time.Instant

data class TeamDto(
    var id: Long? = 0,
    var teamName: String = "",
    var created: Instant? = Clock.systemDefaultZone().instant(),
    var updated: Instant? = Clock.systemDefaultZone().instant(),
    var status: Status = Status.NONE,
    var coaches:List<ParticipantDto> = emptyList(),
    var partisipants:List<ParticipantDto> = emptyList(),
) {
    companion object {
        fun fromTeam(team: Team) = TeamDto(
            id = team.id,
            teamName = team.teamName,
            created = team.created,
            updated = team.updated,
            status = team.status,
//            coaches = team.
        )

        fun toTeam(teamDto: TeamDto) = Team(
            id = teamDto.id ?: 0,
            teamName = teamDto.teamName,
        )
    }
}
