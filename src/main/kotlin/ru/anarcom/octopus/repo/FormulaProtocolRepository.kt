package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.entity.Status

interface FormulaProtocolRepository : JpaRepository<FormulaProtocol, Long> {
    fun getAllByCategory(category: Category): List<FormulaProtocol>
    fun getAllByCategoryAndStatusNot(category: Category, status: Status): List<FormulaProtocol>
    fun getOneByCategoryAndId(category: Category, id: Long): FormulaProtocol
}
