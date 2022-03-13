package ru.anarcom.octopus.dto.team

import com.fasterxml.jackson.annotation.JsonProperty
import ru.anarcom.octopus.entity.Participant
import ru.anarcom.octopus.entity.ParticipantRole
import ru.anarcom.octopus.entity.Status
import java.time.Clock
import java.time.Instant

data class ParticipantDto(
    var id: Long = 0,

    var name: String = "",

    var created: Instant = Clock.systemDefaultZone().instant(),

    var updated: Instant = Clock.systemDefaultZone().instant(),

    var status: Status = Status.NONE,

    @field:JsonProperty("team_role")
    var teamRole: String = ParticipantRole.PARTICIPANT.toString(),
) {
    companion object {
        fun fromParticipant(participant: Participant) = ParticipantDto(
            id = participant.id,
            name = participant.name,
            created = participant.created,
            updated = participant.updated,
            status = participant.status,
            teamRole = participant.teamRole.toString(),
        )
        fun fromParticipant(participants:List<Participant>) = participants.map {
            fromParticipant(it)
        }

    }
}
