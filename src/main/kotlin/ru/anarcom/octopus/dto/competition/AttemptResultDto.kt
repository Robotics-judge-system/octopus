package ru.anarcom.octopus.dto.competition

import com.fasterxml.jackson.annotation.JsonProperty
import ru.anarcom.octopus.entity.AttemptResult
import ru.anarcom.octopus.entity.AttemptResultCalculationStatus
import ru.anarcom.octopus.entity.Status
import java.time.Clock
import java.time.Instant

class AttemptResultDto(
    var id: Long = 0,
    var created: Instant = Clock.systemDefaultZone().instant(),
    var updated: Instant = Clock.systemDefaultZone().instant(),
    var status: String = Status.NONE.name,

    @field:JsonProperty("team_id")
    var teamId: Long = 0,

    @field:JsonProperty("formula_protocol_id")
    var formulaProtocolId: Long = 0,

    @field:JsonProperty("last_edited_by_id")
    var lastEditedById: Long = 0,

    @field:JsonProperty("attempt_id")
    var attemptId: Long = 0,

    @field:JsonProperty("calculation_status")
    var calculationStatus: String = AttemptResultCalculationStatus.NOT_CALCULATED.name,

    @field:JsonProperty("attempt_time")
    var attemptTime: Long? = null,

    @field:JsonProperty("attempt_score")
    var attemptScore: Long? = null,

    @field:JsonProperty("attempt_data")
    var attemptData: Map<String, Int> = mapOf(),
) {
    companion object {
        fun fromAttemptResult(attemptResult: AttemptResult) =
            AttemptResultDto(
                id = attemptResult.id,
                created = attemptResult.created,
                updated = attemptResult.updated,
                status = attemptResult.status.name,
                teamId = attemptResult.team.id,
                formulaProtocolId = attemptResult.formulaProtocol.id,
                lastEditedById = attemptResult.user.id,
                attemptId = attemptResult.attempt.id,
                calculationStatus = attemptResult.calculationStatus.name,
                attemptTime = attemptResult.attemptTime,
                attemptScore = attemptResult.attemptScore,
                attemptData = attemptResult.attemptData,
            )

        fun fromAttemptResult(attemptResults: List<AttemptResult>) =
            attemptResults.map { fromAttemptResult(it) }

    }
}
