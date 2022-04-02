package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.AttemptResult
import ru.anarcom.octopus.entity.FormulaProtocol

interface AttemptResultService {
    fun save(attempt: AttemptResult): AttemptResult
    fun saveNew(attempt: AttemptResult): AttemptResult
    fun isAttemptCanBeDeactivated(attempt: Attempt): Boolean
    fun isFormulaProtocolCanBeDeleted(formulaProtocol: FormulaProtocol):Boolean
}
