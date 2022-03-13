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

    fun getAllByCategory(category: Category): List<Team>

    fun getAllActiveByCategory(category: Category): List<Team>

    fun getByIdAndCategory(id: Long, category: Category): Team

    fun deleteByIdAndCategory(id: Long, category: Category): Team

    fun renameTeam(id: Long, category: Category, newName: String): Team

    fun getByIdAndCategoryOrThrow(id: Long, category: Category): Team
}
