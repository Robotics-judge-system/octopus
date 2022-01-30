package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Participant
import ru.anarcom.octopus.entity.Team

interface TeamService {
    /**
     * Registers team (and adds participants)
     */
    fun addTeam(category: Category, teamName: String, participants: MutableList<Participant>): Team

    fun getByCategoryAndId(category: Category, id:Long) : Team
}
