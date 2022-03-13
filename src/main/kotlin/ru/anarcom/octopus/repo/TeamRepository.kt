package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.Team

@Repository
interface TeamRepository : JpaRepository<Team, Long> {
    fun getByIdAndCategory(id: Long, category: Category): Team
    fun findAllByCategory(category: Category): List<Team>
    fun findAllByCategoryAndStatusNot(category: Category, status: Status): List<Team>
    fun getByCategoryAndId(category: Category, id: Long): Team
    fun findByCategoryAndId(category: Category, id: Long): Team?
}
