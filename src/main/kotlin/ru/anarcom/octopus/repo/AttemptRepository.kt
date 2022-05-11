package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.entity.Status

interface AttemptRepository : JpaRepository<Attempt, Long> {
    fun getAllByCategoryAndStatusNot(category: Category, status: Status): List<Attempt>
    fun findOneByCategoryAndId(category: Category, id: Long): Attempt?
    fun countAllByFormulaProtocolAndStatusNot(
        formulaProtocol: FormulaProtocol,
        status: Status
    ): Long
}
