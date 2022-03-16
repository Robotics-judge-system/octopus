package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.team.TeamDto
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.facade.TeamParticipantFacade
import ru.anarcom.octopus.service.TeamService

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}/team")
class TeamController(
    private val teamService: TeamService,
    private val categoryFacade: CategoryFacade,
    private val teamParticipantFacade: TeamParticipantFacade
) {
    @PostMapping("register")
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

    @GetMapping
    fun getAllByCompetition(
        @PathVariable("competition_id") compId: Long,
        @PathVariable("category_id") catId: Long,
    ): List<TeamDto> {
        val category = categoryFacade.getOneCategory(catId, compId)
        return TeamDto.fromTeam(teamParticipantFacade.getAllNotDeletedByCategory(category))
    }

    @GetMapping("{team_id}")
    fun getOneTeamById(
        @PathVariable("competition_id") compId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("team_id") teamId: Long
    ): TeamDto {
        val category = categoryFacade.getOneCategory(catId, compId)
        return TeamDto.fromTeam(teamParticipantFacade.getOneByIdAndCategory(teamId, category))
    }

    @DeleteMapping("{team_id}")
    fun deleteTeam(
        @PathVariable("competition_id") compId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("team_id") teamId: Long
    ): TeamDto {
        val category = categoryFacade.getOneCategory(catId, compId)
        return TeamDto.fromTeam(teamParticipantFacade.deleteTeam(teamId, category))
    }

    @PostMapping("{team_id}")
    fun updateName(
        @PathVariable("competition_id") compId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("team_id") teamId: Long,
        @RequestBody teamDto: TeamDto
    ): TeamDto {
        val category = categoryFacade.getOneCategory(catId, compId)
        return TeamDto.fromTeam(
            teamService.renameTeam(teamId, category, teamDto.teamName)
        )
    }
}
