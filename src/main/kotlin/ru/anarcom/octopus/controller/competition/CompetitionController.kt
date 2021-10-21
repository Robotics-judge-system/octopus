package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.CompetitionDto
import ru.anarcom.octopus.service.CompetitionService
import ru.anarcom.octopus.service.UserService
import java.security.Principal

@RestController
@RequestMapping("api/v1/competition")
class CompetitionController(
    private val userService: UserService,
    private val competitionService: CompetitionService,
) {

    @PostMapping("create")
    fun createCompetition(
        principal: Principal,
        @RequestBody competitionDto: CompetitionDto
    ): CompetitionDto = CompetitionDto.fromCompetition(
        competitionService.create(
            user = userService.findByUsernameOrThrow(principal.name),
            name = competitionDto.name,
            dateFrom = competitionDto.dateFrom,
            dateTo = competitionDto.dateTo
        )
    )

    @PostMapping("{id}/update/name")
    fun renameCompetition(
        principal: Principal,
        @PathVariable("id") id: Long,
        @RequestBody competitionDto: CompetitionDto,
    ): CompetitionDto = CompetitionDto.fromCompetition(
        competitionService.rename(
            userService.findByUsernameOrThrow(principal.name),
            id,
            competitionDto.name
        )
    )

    @GetMapping()
    fun getAllCompetitionsForUser(
        principal: Principal,
    ): List<CompetitionDto> = CompetitionDto.fromCompetition(
        competitionService.findAllActiveByUser(
            userService.findByUsernameOrThrow(principal.name)
        )
    )

    @GetMapping("{id}")
    fun getCompetitionById(
        @PathVariable("id") id: Long
    ): CompetitionDto = CompetitionDto.fromCompetition(
        competitionService.getById(id)
    )

    @DeleteMapping("{id}")
    fun deleteCompetitionById(
        principal: Principal,
        @PathVariable("id") id: Long,
    ):CompetitionDto = CompetitionDto.fromCompetition(
        competitionService.deleteByIdAndUser(
            userService.findByUsernameOrThrow(principal.name),
            id
        )
    )
}