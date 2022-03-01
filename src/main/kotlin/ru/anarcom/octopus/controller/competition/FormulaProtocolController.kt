package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.FormulaProtocolDto
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.repo.FormulaProtocolRepository
import ru.anarcom.octopus.service.FormulaProtocolService

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}/formula-protocol")
class FormulaProtocolController(
    private val formulaProtocolService: FormulaProtocolService,
    private val formulaProtocolRepository: FormulaProtocolRepository,
    private val categoryFacade: CategoryFacade
) {
    // add formulaProtocol
    @PostMapping
    fun add(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @RequestBody formulaProtocolDto: FormulaProtocolDto,
    ) = "add one"

    @DeleteMapping("{formula-protocol-id}")
    fun delete(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("formula-protocol-id") formulaId: Long,
    ): FormulaProtocolDto {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        var activeFormula = formulaProtocolRepository.getOneByCategoryAndId(category, formulaId)
        if (activeFormula.status != Status.DELETED) {
            activeFormula.status = Status.DELETED
            activeFormula = formulaProtocolService.save(activeFormula)
        }
        return FormulaProtocolDto.fromFormulaProtocol(
            activeFormula
        )
    }

    @GetMapping
    fun getAll(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
    ): List<FormulaProtocolDto> {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        val fp = formulaProtocolRepository.getAllByCategoryAndStatusNot(category, Status.DELETED)

        return FormulaProtocolDto.fromFormulaProtocol(fp)
    }

    @GetMapping("{formula-protocol-id}")
    fun getOne(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("formula-protocol-id") formulaId: Long,
    ): FormulaProtocolDto {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        return FormulaProtocolDto.fromFormulaProtocol(
            formulaProtocolRepository.getOneByCategoryAndId(category, formulaId)
        )
    }

    // update formulaProtocol
    @PostMapping("{formula-protocol-id}")
    fun update(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("formula-protocol-id") formulaId: Long,
        @RequestBody formulaProtocolDto: FormulaProtocolDto,
    ) = "update one"
}
