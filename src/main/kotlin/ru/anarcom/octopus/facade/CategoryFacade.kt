package ru.anarcom.octopus.facade

import ru.anarcom.octopus.dto.competition.CategoryDto
import ru.anarcom.octopus.entity.Category

interface CategoryFacade {
    /**
     * Adds Category for competition.
     */
    fun addCategory(categoryDto: CategoryDto, competitionId: Long): Category

    /**
     * Deletes category by competition and categoryId.
     */
    fun deleteCategory(categoryId: Long, competitionId: Long): Category

    /**
     * Updates category by each != field.
     */
    fun updateCategory(
        categoryId: Long,
        competitionId: Long,
        categoryDto: CategoryDto
    ): Category

    /**
     * Gets list of categories by competition.
     */
    fun getByCompetition(
        competitionId: Long,
    ): List<Category>

    /**
     * Gets one category by id from competition.
     */
    fun getOneCategory(
        categoryId: Long,
        competitionId: Long
    ):Category
}