package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.User
import java.time.Instant

interface CompetitionService {
    fun deleteById(id: Long)

    fun delete(competition: Competition)

    fun create(
        user: User,
        name: String,
        dateFrom: Instant? = null,
        dateTo: Instant? = null,
    ): Competition

    fun findByUser(user: User): List<Competition>

    fun hardDelete(competition: Competition)

    fun hardDeleteById(id: Long)
}
