package ru.anarcom.octopus.service.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.dto.competition.CategoryDto
import ru.anarcom.octopus.entity.Category
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.repo.CategoryRepository
import ru.anarcom.octopus.service.CategoryService
import java.time.Clock
import java.time.Instant

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val clock: Clock
) : CategoryService {
    override fun addCategory(categoryDto: CategoryDto, competition: Competition): Category =
        save(
            Category(
                competition = competition,
                name = categoryDto.name!!,
                dateFrom = categoryDto.dateFrom,
                dateTo = categoryDto.dateTo,
                created = clock.instant(),
                updated = clock.instant(),
                status = Status.ACTIVE,
            )
        )

    override fun deleteCategory(categoryId: Long, competition: Competition): Category {
        val category = categoryRepository.getCategoryByIdAndCompetition(categoryId, competition)
        category.status = Status.DELETED
        return save(category)
    }

    override fun updateCategory(
        categoryId: Long,
        competition: Competition,
        name: String?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Category {
        val category = categoryRepository.getCategoryByIdAndCompetition(categoryId, competition)

        if (name != null) {
            category.name = name
        }

        if (dateFrom != null) {
            category.dateFrom = dateFrom
        }

        if (dateTo != null) {
            category.dateTo = dateTo
        }

        return save(category)
    }

    override fun getByCompetition(competition: Competition): List<Category> =
        categoryRepository.getAllByCompetitionAndStatus(
            competition,
            Status.ACTIVE,
        )

    override fun getOneCategory(categoryId: Long, competition: Competition): Category =
        categoryRepository.getOneByIdAndCompetition(categoryId, competition)

    /**
     * updates instant and updates 'updated' field
     */
    fun save(category: Category): Category {
        category.updated = clock.instant()
        return categoryRepository.save(category)
    }
}
