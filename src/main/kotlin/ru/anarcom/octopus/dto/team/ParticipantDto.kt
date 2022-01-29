package ru.anarcom.octopus.dto.team

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
    var teamRole: ParticipantRole = ParticipantRole.PARTICIPANT,
) {
    companion object {
        fun fromParticipant(participant: Participant) = ParticipantDto(
            id = participant.id,
            name = participant.name,
            created = participant.created,
            updated = participant.updated,
            status = participant.status,
            teamRole = participant.teamRole,
        )

        fun toParticipant(participantDto: ParticipantDto) = Participant(
            id = participantDto.id,
            name = participantDto.name,
            teamRole = participantDto.teamRole
        )
    }
}
