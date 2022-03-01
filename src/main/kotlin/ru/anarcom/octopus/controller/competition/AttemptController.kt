package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.AttemptDto
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.exceptions.CannotActivateException
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.repo.AttemptRepository
import ru.anarcom.octopus.repo.FormulaProtocolRepository
import ru.anarcom.octopus.service.AttemptService

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}/attempt")
class AttemptController(
    private val attemptService: AttemptService,
    private val attemptRepository: AttemptRepository,
    private val categoryFacade: CategoryFacade,
    private val formulaProtocolRepository: FormulaProtocolRepository,
) {
    // get all
    @GetMapping
    fun gelAll(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
    ): List<AttemptDto> {
        val category = categoryFacade.getOneCategory(comId, catId)
        return AttemptDto.fromAttempt(
            attemptRepository.getAllByCategoryAndStatusNot(category, Status.DELETED)
        )
    }

    // get one
    @GetMapping("{attempt_id}")
    fun getOne(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(comId, catId)
        return AttemptDto.fromAttempt(
            attemptRepository.getOneByCategoryAndId(category, attemptId)
        )
    }

    // create new
    @PostMapping
    fun addNew(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
    ) = "add new"

    // delete
    @DeleteMapping("{attempt_id}")
    fun delete(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(comId, catId)
        var attempt = attemptRepository.getOneByCategoryAndId(category, attemptId)
        if (attempt.status != Status.DELETED) {
            attempt = attemptService.save(attempt)
        }
        return AttemptDto.fromAttempt(attempt)
    }

    // update
    @PostMapping("{attempt_id}")
    fun update(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
    ) = "update"

    // formulaProtocol to this
    // TODO добавить способ поставить null для formulaProtocol (действие должно работать, если нет
    // результатов или их статус == DELETED)
    @PostMapping("{attempt_id}/attach/formula-protocol/{formula_protocol_id}")
    fun attachFormulaProtocol(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
        @PathVariable("formula_protocol_id") formulaProtocolId: Long,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(comId, catId)
        val attempt = attemptRepository.getOneByCategoryAndId(category, attemptId)
        val formula = formulaProtocolRepository.getOneByCategoryAndId(category, formulaProtocolId)
        attempt.formulaProtocol = formula
        return AttemptDto.fromAttempt(
            attemptRepository.save(attempt)
        )
    }

    // activate/deactivate
    @PostMapping("{attempt_id}/{status}")
    fun activateDeactivate(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
        @PathVariable("status") newStatus: String,
    ): AttemptDto? {
        val category = categoryFacade.getOneCategory(comId, catId)
        val attempt = attemptRepository.getOneByCategoryAndId(category, attemptId)
        if (attempt.formulaProtocol == null) {
            throw CannotActivateException("formula-protocol is null")
        }
        val isActive = when (newStatus) {
            "activate" -> true
            "deactivate" -> false
            else -> throw CannotActivateException("status not in ['activate', 'deactivate']")
        }

        if (isActive != attempt.isActive) {
            if(isActive){
                attempt.isActive = true
            } else {
                // TODO проверка на наличие существующих результатов
            }
        }
        return AttemptDto.fromAttempt(attempt)
    }
}
