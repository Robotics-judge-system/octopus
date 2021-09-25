package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.User
import java.time.Instant

interface CompetitionService {
    fun deleteById(id: Long):Competition

    fun delete(competition: Competition):Competition

    fun create(
        user: User,
        name: String,
        dateFrom: Instant? = null,
        dateTo: Instant? = null,
    ): Competition

    fun findAllActiveByUser(user: User): List<Competition>

    fun hardDelete(competition: Competition)

    fun hardDeleteById(id: Long)

    fun rename(user:User, id:Long, name: String):Competition

    fun getById(id:Long):Competition

    fun deleteByIdAndUser(user: User, id: Long):Competition
}
