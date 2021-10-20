package ru.anarcom.octopus.dto.competition

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Status
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = false)
class CategoryDto(
    var id: Long? = null,

    @field:JsonProperty("competition_id")
    var competitionId: Long? = null,

    var competition: CompetitionDto? = null,

    var name: String? = null,

    @field:JsonProperty("date_from")
    var dateFrom: Instant? = null,

    @field:JsonProperty("date_to")
    var dateTo: Instant? = null,

    var created: Instant? = null,

    var updated: Instant? = null,

    var status: String = Status.NONE.toString(),
) {
    companion object {
        fun fromCategory(category: Category): CategoryDto =
            CategoryDto(
                id = category.id,
                competitionId = category.competition.id,
                competition = CompetitionDto.fromCompetition(category.competition),
                name = category.name,
                dateFrom = category.dateFrom,
                dateTo = category.dateTo,
                created = category.created,
                updated = category.updated,
                status = category.status.toString(),
            )

        fun fromCategory(categories: List<Category>): List<CategoryDto> =
            categories.map { fromCategory(it) }

    }
}
