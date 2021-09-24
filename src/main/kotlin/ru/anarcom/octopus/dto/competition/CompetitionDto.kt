package ru.anarcom.octopus.dto.competition

import com.fasterxml.jackson.annotation.JsonProperty
import ru.anarcom.octopus.entity.Competition
import java.time.Instant


data class CompetitionDto(
    var id: Long,
    var name: String,
    var created: Instant?=null,
    var updated: Instant?=null,
    @field:JsonProperty("date_to")
    var dateTo: Instant?=null,
    @field:JsonProperty("date_from")
    var dateFrom: Instant?=null,
){
    companion object{
        fun fromCompetition(competition: Competition) = CompetitionDto(
            id = competition.id,
            name = competition.name,
            created = competition.created,
            updated = competition.updated,
            dateTo = competition.dateTo,
            dateFrom = competition.dateFrom
        )
    }
}
