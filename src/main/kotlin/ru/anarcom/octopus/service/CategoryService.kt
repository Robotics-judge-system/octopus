package ru.anarcom.octopus.service

import ru.anarcom.octopus.dto.competition.CategoryDto
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Competition
import java.time.Instant

interface CategoryService {
    /**
     * Adds Category for competition.
     */
    fun addCategory(categoryDto: CategoryDto, competition: Competition): Category

    /**
     * Deletes category by competition and categoryId.
     */
    fun deleteCategory(categoryId: Long, competition: Competition): Category

    /**
     * Updates category by each != field.
     */
    fun updateCategory(
        categoryId: Long,
        competition: Competition,
        name: String? = null,
        dateFrom: Instant? = null,
        dateTo: Instant? = null
    ): Category

    /**
     * Gets list of categories by competition.
     */
    fun getByCompetition(
        competition: Competition
    ):List<Category>

    /**
     * Gets one category by id from competition.
     */
    fun getOneCategory(
        categoryId: Long,
        competition: Competition
    ):Category
}
