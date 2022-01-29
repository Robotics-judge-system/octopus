package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.anarcom.octopus.entity.Participant

@Repository
interface ParticipantRepository:JpaRepository<Participant, Long> {
}
