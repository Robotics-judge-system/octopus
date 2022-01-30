package ru.anarcom.octopus.service.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Participant
import ru.anarcom.octopus.entity.Team
import ru.anarcom.octopus.repo.ParticipantRepository
import ru.anarcom.octopus.service.ParticipantService
import java.time.Clock

@Service
class ParticipantServiceImpl(
    val clock: Clock,
    val participantRepository: ParticipantRepository
) : ParticipantService {
    private fun save(participant: Participant): Participant{
        participant.updated = clock.instant()
        return participantRepository.save(participant)
    }

    override fun add(participant: Participant): Participant {
        participant.created = clock.instant()
        return save(participant)
    }

    override fun getByTeam(team: Team): List<Participant> {
        return participantRepository.getAllByTeam(team)
    }
}
