package ru.anarcom.octopus.dto.competition

import com.fasterxml.jackson.annotation.JsonProperty
import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.Status
import java.time.Clock
import java.time.Instant

class AttemptDto(
    var id: Long = 0,
    var name: String = "",
    var category: CategoryDto? = null,
    @field:JsonProperty("formula_protocol")
    var formulaProtocol: FormulaProtocolDto? = null,

    @field:JsonProperty("is_active")
    var isActive: Boolean = false,

    var created: Instant = Clock.systemDefaultZone().instant(),
    var updated: Instant = Clock.systemDefaultZone().instant(),

    var status: String = Status.NONE.name,
) {
    companion object {
        fun fromAttempt(attempt: Attempt) = AttemptDto(
            id = attempt.id,
            name = attempt.name,
            category = CategoryDto.fromCategory(attempt.category),
            formulaProtocol = when(attempt.formulaProtocol){
                null -> null
                else -> FormulaProtocolDto.fromFormulaProtocol(attempt.formulaProtocol!!)
            },
            isActive = attempt.isActive,
            created = attempt.created,
            updated = attempt.updated,
            status = attempt.status.name
        )

        fun fromAttempt(attempts: List<Attempt>) = attempts.map { fromAttempt(it) }
    }
}
