package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Competition

interface CompetitonRepository:JpaRepository<Competition, Long> {
}