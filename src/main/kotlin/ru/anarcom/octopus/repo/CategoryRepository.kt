package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.Status

interface CategoryRepository : JpaRepository<Category, Long> {
    fun getCategoryByIdAndCompetition(
        id: Long,
        competition: Competition
    ): Category

    fun getAllByCompetitionAndStatus(
        competition: Competition,
        status: Status
    ): List<Category>

    fun getOneByIdAndCompetition(
        id:Long,
        competition: Competition
    ):Category
}
