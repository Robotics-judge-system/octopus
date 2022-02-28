package ru.anarcom.octopus.dto.competition

import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.entity.Status
import java.time.Clock
import java.time.Instant

class FormulaProtocolDto(
    var id: Long = 0,
    var name: String = "",
    var category: CategoryDto? = null,
    var created: Instant = Clock.systemDefaultZone().instant(),
    var updated: Instant = Clock.systemDefaultZone().instant(),
    var status: String = Status.NONE.name,
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
            )

        fun fromFormulaProtocol(formulaProtocols: List<FormulaProtocol>) =
            formulaProtocols.map {
                fromFormulaProtocol(it)
            }
    }
}
