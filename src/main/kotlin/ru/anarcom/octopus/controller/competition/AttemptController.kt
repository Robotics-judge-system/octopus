package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.AttemptDto
import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.exceptions.CannotActivateException
import ru.anarcom.octopus.exceptions.UpdateAttemptException
import ru.anarcom.octopus.exceptions.ValidationException
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.repo.AttemptRepository
import ru.anarcom.octopus.repo.FormulaProtocolRepository
import ru.anarcom.octopus.service.AttemptResultService
import ru.anarcom.octopus.service.AttemptService

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}/attempt")
class AttemptController(
    private val attemptService: AttemptService,
    private val attemptRepository: AttemptRepository,
    private val categoryFacade: CategoryFacade,
    private val formulaProtocolRepository: FormulaProtocolRepository,
    private val attemptResultService: AttemptResultService
) {
    @GetMapping
    fun gelAll(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
    ): List<AttemptDto> {
        val category = categoryFacade.getOneCategory(catId, comId)
        return AttemptDto.fromAttempt(
            attemptRepository.getAllByCategoryAndStatusNot(category, Status.DELETED)
        )
    }

    @GetMapping("{attempt_id}")
    fun getOne(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(catId, comId)
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)
        return AttemptDto.fromAttempt(
            attempt
        )
    }

    @PostMapping
    fun addNew(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @RequestBody attemptDto: AttemptDto,
    ): AttemptDto {
        if (attemptDto.name.isBlank() || attemptDto.name.isEmpty()) {
            throw ValidationException("name should not be blank or empty")
        }
        val category = categoryFacade.getOneCategory(catId, comId)
        return AttemptDto.fromAttempt(
            attemptService.saveNew(
                Attempt(
                    id = 0,
                    category = category,
                    isActive = false,
                    name = attemptDto.name,
                    status = Status.ACTIVE
                )
            )
        )
    }

    @DeleteMapping("{attempt_id}")
    fun delete(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(catId, comId)
        var attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)
        if (attempt.status != Status.DELETED) {
            attempt.status = Status.DELETED
            attempt = attemptService.save(attempt)
        }
        return AttemptDto.fromAttempt(attempt)
    }

    @PostMapping("{attempt_id}")
    fun update(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
        @RequestBody attemptDto: AttemptDto,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(catId, comId)
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)
        if(attemptDto.name.isNotBlank() && attemptDto.name.isNotEmpty()){
            attempt.name = attemptDto.name
        } else {
            throw  ValidationException("name should not be empty or blank")
        }
        return AttemptDto.fromAttempt(
            attemptService.save(attempt)
        )
    }

    @PostMapping("{attempt_id}/attach/formula-protocol/{formula_protocol_id}")
    fun attachFormulaProtocol(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
        @PathVariable("formula_protocol_id") formulaProtocolId: Long,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(catId, comId)
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)
        val formula = formulaProtocolRepository.getOneByCategoryAndId(category, formulaProtocolId)
        if (formula.status == Status.DELETED) {
            throw ValidationException("Formula-protocol is already deleted")
        }
        if (attempt.isActive) {
            throw UpdateAttemptException("Attempt is active")
        }
        attempt.formulaProtocol = formula
        return AttemptDto.fromAttempt(
            attemptService.save(attempt)
        )
    }

    @PostMapping("{attempt_id}/attach/formula-protocol/null")
    fun attachNullFormulaProtocol(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
    ): AttemptDto {
        val category = categoryFacade.getOneCategory(catId, comId)
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)

        if (attempt.status == Status.DELETED) {
            throw ValidationException("Attempt is already deleted")
        }

        if (attempt.isActive) {
            throw UpdateAttemptException("Attempt is active")
        }

        if (attempt.formulaProtocol == null) {
            return AttemptDto.fromAttempt(attempt)
        }

        attempt.formulaProtocol = null
        return AttemptDto.fromAttempt(
            attemptService.save(attempt)
        )
    }

    @PostMapping("{attempt_id}/{status}")
    fun activateDeactivate(
        @PathVariable("competition_id") comId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("attempt_id") attemptId: Long,
        @PathVariable("status") newStatus: String,
    ): AttemptDto? {
        val category = categoryFacade.getOneCategory(catId, comId)
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)
        if(attempt.status == Status.DELETED){
            throw CannotActivateException("attempt is deleted")
        }
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
                if(!attemptResultService.isAttemptCanBeDeactivater(attempt)){
                    throw CannotActivateException("Attempt has not deleted results")
                }
                attempt.isActive = false
            }
        }
        return AttemptDto.fromAttempt(
            attemptService.save(attempt)
        )
    }
}
