package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Participant
import ru.anarcom.octopus.entity.Team

interface ParticipantService {
    fun add(participant: Participant): Participant
    fun getByTeam(team:Team):List<Participant>
}