package ru.anarcom.octopus.dto.competition

import com.fasterxml.jackson.annotation.JsonProperty
import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.protocol.ProtocolFieldDescription
import java.time.Clock
import java.time.Instant

class FormulaProtocolDto(
    var id: Long = 0,
    var name: String = "",
    var category: CategoryDto? = null,
    var created: Instant = Clock.systemDefaultZone().instant(),
    var updated: Instant = Clock.systemDefaultZone().instant(),
    var status: String = Status.NONE.name,
    @set:JsonProperty("protocol_description")
    var protocolDescription: List<ProtocolFieldDescription>
) {
    companion object {
        fun fromFormulaProtocol(formulaProtocol: FormulaProtocol) =
            FormulaProtocolDto(
                id = formulaProtocol.id,
                name = formulaProtocol.name,
                category = CategoryDto.fromCategory(formulaProtocol.category),
                created = formulaProtocol.created,
                updated = formulaProtocol.updated,
                status = formulaProtocol.status.name,
                protocolDescription = formulaProtocol.protocolDescription,
            )

        fun fromFormulaProtocol(formulaProtocols: List<FormulaProtocol>) =
            formulaProtocols.map {
                fromFormulaProtocol(it)
            }
    }
}
