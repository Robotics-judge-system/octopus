package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Status

interface AttemptRepository : JpaRepository<Attempt, Long> {
    fun getAllByCategoryAndStatusNot(category: Category, status: Status): List<Attempt>
    fun getOneByCategoryAndId(category: Category, id: Long): Attempt
}
