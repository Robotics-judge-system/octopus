package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.team.TeamDto
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.facade.TeamParticipantFacade
import ru.anarcom.octopus.service.TeamService

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}")
class TeamController(
    private val teamService: TeamService,
    private val categoryFacade: CategoryFacade,
    private val teamParticipantFacade: TeamParticipantFacade
) {
    //TODO: register
    @GetMapping("register")
    fun registerTeam(
        @PathVariable("competition_id") compId: Long,
        @PathVariable("category_id") catId: Long,
        @RequestBody teamDto: TeamDto
    ): TeamDto {
        val a = categoryFacade.getOneCategory(catId, compId)
        return TeamDto.fromTeam(
            teamParticipantFacade.registerTeam(
                a,
                teamDto.teamName,
                teamDto.participants.toMutableList()
            )
        )
    }


    //TODO: get one by id
    //TODO: get all by competition
    //TODO: DELETE
    //TODO: update
}
