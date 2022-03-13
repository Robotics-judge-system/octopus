package ru.anarcom.octopus.calculation

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.FormulaProtocol

@Service
class FormulaProtocolValidator {
    /**
     * @return true if attemptData is valid for Formula-Protocol
     */
    fun validate(
        formulaProtocol: FormulaProtocol,
        attemptData:Map<String, String>
    ) : Boolean {
        // TODO implement
        return true
    }
}
