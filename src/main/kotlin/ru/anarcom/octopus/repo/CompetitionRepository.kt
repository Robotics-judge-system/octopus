package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User

interface CompetitionRepository : JpaRepository<Competition, Long> {
    fun findAllByUser(user: User): List<Competition>
    fun findOneById(id: Long): Competition?
    fun findAllByUserAndStatus(user: User, status: Status):List<Competition>
}