package ru.anarcom.octopus.facade

import ru.anarcom.octopus.dto.competition.CompetitionDto
import ru.anarcom.octopus.entity.Competition
import java.security.Principal

interface CompetitionFacade {
    fun deleteById(id: Long): Competition

    fun delete(competition: Competition): Competition

    fun create(
        principal: Principal,
        competitionDto: CompetitionDto
    ): Competition

    fun findAllActiveByUser(principal: Principal): List<Competition>

    fun hardDelete(competition: Competition)

    fun hardDeleteById(id: Long)

    fun rename(principal: Principal, id: Long, name: String): Competition

    fun getById(id: Long): Competition

    fun deleteByIdAndUser(principal: Principal, id: Long): Competition
}
