package ru.anarcom.octopus.calculation

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.entity.protocol.ProtocolFieldType

@Service
class FormulaProtocolValidator {
    /**
     * @return true if attemptData is valid for Formula-Protocol
     */
    fun validate(
        formulaProtocol: FormulaProtocol,
        attemptData: Map<String, Int>
    ): Boolean {
        val protocolData = formulaProtocol.protocolDescription
            .filter {
                it.type != ProtocolFieldType.SEPARATOR
            }
            .map {
                it.name
            }

        return attemptData
            .keys
            .map {
                protocolData.contains(it)
            }
            .reduce { acc, b ->
                acc && b
            }
        // TODO add validation for min and max values
        // + add tests with no db (like node tests)
    }
}
