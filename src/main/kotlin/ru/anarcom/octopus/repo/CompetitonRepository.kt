package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.User

interface CompetitonRepository:JpaRepository<Competition, Long> {
    fun findAllByUser(user: User): List<Competition>
}