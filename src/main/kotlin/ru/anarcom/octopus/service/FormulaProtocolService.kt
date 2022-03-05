package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.FormulaProtocol

interface FormulaProtocolService {
    fun save(formulaProtocol: FormulaProtocol): FormulaProtocol
    fun saveNew(formulaProtocol: FormulaProtocol): FormulaProtocol
}
