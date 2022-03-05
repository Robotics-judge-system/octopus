package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.AttemptResultDto
import ru.anarcom.octopus.entity.AttemptResult
import ru.anarcom.octopus.entity.AttemptResultCalculationStatus
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.exceptions.NotFoundException
import ru.anarcom.octopus.exceptions.UpdateAttemptResultException
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.repo.AttemptResultRepository
import ru.anarcom.octopus.repo.TeamRepository
import ru.anarcom.octopus.service.AttemptResultService
import ru.anarcom.octopus.service.AttemptService
import ru.anarcom.octopus.service.UserService
import java.security.Principal

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}/team/{team_id}/attempt")
class AttemptResultController(
    private val attemptResultRepository: AttemptResultRepository,
    private val categoryFacade: CategoryFacade,
    private val attemptService: AttemptService,
    private val teamRepository: TeamRepository,
    private val userService: UserService,
    private val attemptResultService: AttemptResultService,
) {

    // create\update
    @PostMapping("{attempt_id}")
    fun createAndUpdate(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("team_id") teamId: Long,
        @PathVariable("attempt_id") attemptId: Long,
        @RequestBody attemptData: Map<String, String>,
        principal: Principal,
    ): AttemptResultDto {
        // TODO добавить проверку на активность attempt -> else throw exception
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        val team = teamRepository.findById(teamId).orElseThrow()
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)
        val user = userService.findByUsernameOrThrow(principal.name)

        if (team.category!!.id != attempt.category.id) {
            throw UpdateAttemptResultException("This team is not from this category")
            // TODO перенести получение команды и выбрасывание ошибки в сервис команд
        }

        var attemptResult: AttemptResult
        // TODO добавить andStatusNotIn
        if (attemptResultRepository.existsByAttemptAndTeam(attempt, team)) {
            attemptResult = attemptResultRepository.getByAttemptAndTeam(attempt, team)
            attemptResult.attemptData = attemptData
            attemptResult.calculationStatus = AttemptResultCalculationStatus.NOT_CALCULATED
            attemptResult.attemptScore = null
            attemptResult.attemptTime = null
            attemptResult = attemptResultService.save(attemptResult)
        } else {
            attemptResult = AttemptResult(
                attempt = attempt,
                team = team,
                formulaProtocol = attempt.formulaProtocol!!,
                attemptScore = null,
                attemptTime = null,
                attemptData = attemptData,
                user = user,
                status = Status.ACTIVE,
                calculationStatus = AttemptResultCalculationStatus.NOT_CALCULATED
            )
            attemptResult = attemptResultService.saveNew(attemptResult)
        }

        if (attemptResult.attemptData.containsKey("a")) {
            attemptResult.attemptScore = attemptData["a"]?.toLong()?.plus(1)
            attemptResult.attemptTime = 123
        } else {
            attemptResult.attemptScore = 12
            attemptResult.attemptTime = 124
        }

        attemptResult.calculationStatus = AttemptResultCalculationStatus.CALCULATED
        return AttemptResultDto.fromAttemptResult(
            attemptResultService.save(attemptResult)
        )
    }

    @DeleteMapping("{attempt_id}")
    fun deleteAttemptResult(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("team_id") teamId: Long,
        @PathVariable("attempt_id") attemptId: Long,
        principal: Principal,
    ): AttemptResultDto {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        val team = teamRepository.findById(teamId).orElseThrow()
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)
        val user = userService.findByUsernameOrThrow(principal.name)

        if (team.category!!.id != attempt.category.id) {
            throw UpdateAttemptResultException("This team is not from this category")
            // TODO перенести получение команды и выбрасывание ошибки в сервис команд
        }
        if (!attemptResultRepository.existsByAttemptAndTeam(attempt, team)) {
            throw NotFoundException("No attemptResults for this team in that attempt")
        }

        val attemptResult = attemptResultRepository.getByAttemptAndTeam(attempt, team)
        attemptResult.status = Status.DELETED
        attemptResult.user = user
        return AttemptResultDto.fromAttemptResult(
            attemptResultService.save(attemptResult)
        )
    }

    //    getAllByTeam
    @GetMapping
    fun getAllResultByTeam(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("team_id") teamId: Long,
        principal: Principal,
    ): List<AttemptResultDto> {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        val team = teamRepository.findById(teamId).orElseThrow()
        val results = attemptResultRepository.getAllByTeam(team)
        // TODO выбрасывать 404, если команда не принадлежит категории (+ я бы изменил метод репы)
        return AttemptResultDto.fromAttemptResult(results)
    }

    //    get one by team and attempt
    @GetMapping("{attempt_id}")
    fun getByTeamAndAttempt(
        @PathVariable("competition_id") competitionId: Long,
        @PathVariable("category_id") categoryId: Long,
        @PathVariable("team_id") teamId: Long,
        @PathVariable("attempt_id") attemptId: Long,
    ): AttemptResultDto {
        val category = categoryFacade.getOneCategory(categoryId, competitionId)
        val team = teamRepository.findById(teamId).orElseThrow()
        val attempt = attemptService.findAttemptByCategoryAndIdOrThrow(category, attemptId)

        if (!attemptResultRepository.existsByAttemptAndTeam(attempt, team)) {
            throw NotFoundException("No attemptResults for this team in that attempt")
        }
        // TODO throw if null
        val attemptResult = attemptResultRepository.getByAttemptAndTeamAndStatusNot(
            attempt,
            team,
            Status.DELETED
        )

        return AttemptResultDto.fromAttemptResult(
            attemptResult
        )
    }
}
