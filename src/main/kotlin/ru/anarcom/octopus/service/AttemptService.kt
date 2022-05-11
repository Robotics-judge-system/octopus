package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.FormulaProtocol

interface AttemptService {
    fun save(attempt: Attempt): Attempt
    fun saveNew(attempt: Attempt): Attempt
    fun findAttemptByCategoryAndIdOrThrow(category: Category, id: Long): Attempt
    fun isFormulaProtocolCanBeDeleted(formulaProtocol: FormulaProtocol): Boolean
}
