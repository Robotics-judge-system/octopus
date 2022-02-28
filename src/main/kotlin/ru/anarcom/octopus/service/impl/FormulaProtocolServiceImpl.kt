package ru.anarcom.octopus.service.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.repo.FormulaProtocolRepository
import ru.anarcom.octopus.service.FormulaProtocolService
import java.time.Clock

@Service
class FormulaProtocolServiceImpl(
    private val formulaProtocolServiceRepository: FormulaProtocolRepository,
    private val clock: Clock
) : FormulaProtocolService {
    override fun save(formulaProtocol: FormulaProtocol): FormulaProtocol {
        formulaProtocol.updated = clock.instant()
        return formulaProtocolServiceRepository.save(formulaProtocol)
    }

    override fun saveNew(formulaProtocol: FormulaProtocol): FormulaProtocol {
        formulaProtocol.created = clock.instant()
        return save(formulaProtocol)
    }
}
