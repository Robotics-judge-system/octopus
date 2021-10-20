package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.CategoryDto
import ru.anarcom.octopus.facade.CategoryFacade

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category")
class CategoryController(
    private val categoryFacade: CategoryFacade
) {
    @PostMapping("create")
    fun createCategory(
        @PathVariable("competition_id") competitionId: Long,
        @RequestBody categoryDto: CategoryDto
    ): CategoryDto = CategoryDto.fromCategory(
        categoryFacade.addCategory(
            categoryDto,
            competitionId
        )
    )

    @GetMapping("/all")
    fun getCategoryForCompetition(
        @PathVariable("competition_id") competitionId: Long,
    ): List<CategoryDto> = CategoryDto.fromCategory(
        categoryFacade.getByCompetition(competitionId)
    )

    @PostMapping("{category_id}/update")
    fun updateCategoryForCompetition(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @RequestBody categoryDto: CategoryDto
    ): CategoryDto = CategoryDto.fromCategory(
        categoryFacade.updateCategory(categoryId, competitionId, categoryDto)
    )

    @DeleteMapping("{category_id}/delete")
    fun deleteCategoryForCompetition(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
    ): CategoryDto = CategoryDto.fromCategory(
        categoryFacade.deleteCategory(categoryId, competitionId)
    )

    @GetMapping("{category_id}")
    fun getOneCategory(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
    ): CategoryDto = CategoryDto.fromCategory(
        categoryFacade.getOneCategory(categoryId, competitionId)
    )
}
