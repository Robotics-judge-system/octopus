package ru.anarcom.octopus.controller.competition

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.anarcom.octopus.dto.team.ParticipantDto
import ru.anarcom.octopus.facade.CategoryFacade
import ru.anarcom.octopus.repo.ParticipantRepository
import ru.anarcom.octopus.service.ParticipantService
import ru.anarcom.octopus.service.TeamService

@RestController
@RequestMapping("api/v1/competition/{competition_id}/category/{category_id}/team/{team_id}/participant")
class ParticipantController(
    private val participantService: ParticipantService,
    private val teamService: TeamService,
    private val categoryFacade: CategoryFacade,
    private val participantRepository: ParticipantRepository
) {
//    @PostMapping
//    fun addParticipants(
//        @PathVariable("competition_id") compId: Long,
//        @PathVariable("category_id") catId: Long,
//        @PathVariable("team_id") teamId: Long,
//        @RequestBody participantDto: ParticipantDto
//    ): ParticipantDto {
//        val category = categoryFacade.getOneCategory(catId, compId)
//        val team = teamService.getByCategoryAndId(category, teamId)
//        var participant = Participant(
//            team = team,
//            name = participantDto.name,
//            teamRole = ParticipantRole.valueOf(participantDto.teamRole),
//            status = Status.ACTIVE
//        )
//        participant = participantService.add(participant)
//        return ParticipantDto.fromParticipant(participant)
//    }

//    fun deleteParticipant() {
//
//    }

    // TODO implement with method
//    @GetMapping("{participant_id}")
//    fun getOneParticipant(
//        @PathVariable("competition_id") compId: Long,
//        @PathVariable("category_id") catId: Long,
//        @PathVariable("team_id") teamId: Long,
//        @PathVariable("participant_id") partId: Long,
//    ) {
//
//    }

    @GetMapping
    fun getManyParticipants(
        @PathVariable("competition_id") compId: Long,
        @PathVariable("category_id") catId: Long,
        @PathVariable("team_id") teamId: Long,
    ):List<ParticipantDto> {
        val category = categoryFacade.getOneCategory(catId, compId)
        val team = teamService.getByCategoryAndId(category, teamId)
        val participants = participantService.getByTeam(team)
        return ParticipantDto.fromParticipant(participants)
    }

//    fun updateParticipant() {
//
//    }
}