package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.anarcom.octopus.entity.Participant
import ru.anarcom.octopus.entity.Team

@Repository
interface ParticipantRepository:JpaRepository<Participant, Long> {
    fun getAllByTeam(team: Team):List<Participant>
}
