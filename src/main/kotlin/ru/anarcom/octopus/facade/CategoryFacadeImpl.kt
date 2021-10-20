package ru.anarcom.octopus.facade

import org.springframework.stereotype.Service
import ru.anarcom.octopus.dto.competition.CategoryDto
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.service.CategoryService
import ru.anarcom.octopus.service.CompetitionService

@Service
class CategoryFacadeImpl(
    private val categoryService: CategoryService,
    private val competitionService: CompetitionService,
) : CategoryFacade {
    override fun addCategory(categoryDto: CategoryDto, competitionId: Long): Category =
        categoryService.addCategory(
            categoryDto,
            competitionService.getById(competitionId)
        )

    override fun deleteCategory(categoryId: Long, competitionId: Long): Category =
        categoryService.deleteCategory(
            categoryId,
            competitionService.getById(competitionId),
        )

    override fun updateCategory(
        categoryId: Long,
        competitionId: Long,
        categoryDto: CategoryDto
    ): Category =
        categoryService.updateCategory(
            categoryId,
            competitionService.getById(competitionId),
            categoryDto.name,
            categoryDto.dateFrom,
            categoryDto.dateTo,
        )

    override fun getByCompetition(competitionId: Long): List<Category> =
        categoryService.getByCompetition(
            competitionService.getById(competitionId)
        )

    override fun getOneCategory(categoryId: Long, competitionId: Long): Category =
        categoryService.getOneCategory(
            categoryId,
            competitionService.getById(competitionId)
        )

}
