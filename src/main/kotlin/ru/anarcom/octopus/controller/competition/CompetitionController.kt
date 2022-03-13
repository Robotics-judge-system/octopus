package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.competition.CompetitionDto
import ru.anarcom.octopus.facade.CompetitionFacade
import java.security.Principal

@RestController
@RequestMapping("api/v1/competition")
class CompetitionController(
    private val competitionFacade: CompetitionFacade,
) {

    @PostMapping("create")
    fun createCompetition(
        principal: Principal,
        @RequestBody competitionDto: CompetitionDto
    ): CompetitionDto = CompetitionDto.fromCompetition(
        competitionFacade.create(
            principal,
            competitionDto
        )
    )

    @PostMapping("{id}/update/name")
    fun renameCompetition(
        principal: Principal,
        @PathVariable("id") id: Long,
        @RequestBody competitionDto: CompetitionDto,
    ): CompetitionDto = CompetitionDto.fromCompetition(
        competitionFacade.rename(
            principal,
            id,
            competitionDto.name
        )
    )

    @GetMapping
    fun getAllCompetitionsForUser(
        principal: Principal,
    ): List<CompetitionDto> = CompetitionDto.fromCompetition(
        competitionFacade.findAllActiveByUser(
            principal
        )
    )

    @GetMapping("{id}")
    fun getCompetitionById(
        @PathVariable("id") id: Long
    ): CompetitionDto = CompetitionDto.fromCompetition(
        competitionFacade.getById(id)
    )

    @DeleteMapping("{id}")
    fun deleteCompetitionById(
        principal: Principal,
        @PathVariable("id") id: Long,
    ):CompetitionDto = CompetitionDto.fromCompetition(
        competitionFacade.deleteByIdAndUser(
            principal,
            id
        )
    )
}
