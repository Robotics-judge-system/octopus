package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.FormulaProtocolDto
import ru.anarcom.octopus.entity.FormulaProtocol
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.exceptions.ValidationException
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.repo.FormulaProtocolRepository
import ru.anarcom.octopus.service.FormulaProtocolService
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}/formula-protocol")
class FormulaProtocolController(
    private val formulaProtocolService: FormulaProtocolService,
    private val formulaProtocolRepository: FormulaProtocolRepository,
    private val categoryFacade: CategoryFacade
) {
    @PostMapping
    fun add(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @Valid @RequestBody formulaProtocolDto: FormulaProtocolDto,
    ): FormulaProtocolDto {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        if (formulaProtocolDto.name.isBlank()) {
            throw ValidationException("field 'name' should not be null")
        }
        val formulaProtocol = FormulaProtocol(
            id = 0,
            name = formulaProtocolDto.name,
            category = category,
            status = Status.ACTIVE,
            protocolDescription = formulaProtocolDto.protocolDescription,
            formulaDescription = formulaProtocolDto.formulaDescription,
        )
        return FormulaProtocolDto.fromFormulaProtocol(
            formulaProtocolService.saveNew(formulaProtocol)
        )
    }

    @DeleteMapping("{formula-protocol-id}")
    fun delete(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("formula-protocol-id") formulaId: Long,
    ): FormulaProtocolDto {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        var activeFormula = formulaProtocolRepository.getOneByCategoryAndId(category, formulaId)

        /* TODO ???????????????? ??????????????????, ?????? ???????????? ?????????????? ???? ???????????????????????? ??????????,
          (attempt results + attempt). status ?????????????????????????? != DELETED */

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

    @PostMapping("{formula-protocol-id}")
    fun update(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("formula-protocol-id") formulaId: Long,
        @RequestBody formulaProtocolDto: FormulaProtocolDto,
    ): FormulaProtocolDto {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        var formulaProtocol = formulaProtocolRepository.getOneByCategoryAndId(category, formulaId)
        var flag = false
        if(formulaProtocolDto.name.isNotBlank()){
            formulaProtocol.name = formulaProtocolDto.name
            flag = true
        }
        // TODO add validation for new formula-protocol description
        if(formulaProtocolDto.formulaDescription != formulaProtocol.formulaDescription){
            formulaProtocol.formulaDescription = formulaProtocolDto.formulaDescription
            flag = true
        }

        if(formulaProtocolDto.protocolDescription != formulaProtocol.protocolDescription){
            formulaProtocol.protocolDescription = formulaProtocolDto.protocolDescription
            flag = true
        }

        if(flag){
           formulaProtocol = formulaProtocolService.save(formulaProtocol)
        }
        return FormulaProtocolDto.fromFormulaProtocol(formulaProtocol)
    }
}
