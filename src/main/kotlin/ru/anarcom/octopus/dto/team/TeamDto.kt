package ru.anarcom.octopus.dto.team

import com.fasterxml.jackson.annotation.JsonProperty
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.Team
import java.time.Clock
import java.time.Instant

data class TeamDto(
    var id: Long? = 0,

    @field:JsonProperty("team_name")
    var teamName: String = "",

    var created: Instant? = Clock.systemDefaultZone().instant(),

    var updated: Instant? = Clock.systemDefaultZone().instant(),

    var status: Status = Status.NONE,

    var participants: List<ParticipantDto> = emptyList(),
) {
    companion object {
        fun fromTeam(team: Team) = TeamDto(
            id = team.id,
            teamName = team.teamName,
            created = team.created,
            updated = team.updated,
            status = team.status,
            participants = team.participants.map { ParticipantDto.fromParticipant(it) }
        )
        fun fromTeam(teams:List<Team>) = teams.map { Companion.fromTeam(it) }
    }
}
