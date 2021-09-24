package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    ):CompetitionDto = CompetitionDto.fromCompetition(
        competitionService.create(
            user = userService.findByUsername(principal.name)!!,
            name = competitionDto.name,
            dateFrom = competitionDto.dateFrom,
            dateTo = competitionDto.dateTo
        )
    )


}