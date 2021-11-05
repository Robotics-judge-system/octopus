package ru.anarcom.octopus.facade.impl

import org.springframework.stereotype.Service
import ru.anarcom.octopus.dto.competition.CompetitionDto
import ru.anarcom.octopus.entity.Competition
import ru.anarcom.octopus.facade.CompetitionFacade
import ru.anarcom.octopus.service.CompetitionService
import ru.anarcom.octopus.service.UserService
import java.security.Principal

@Service
class CompetitionFacadeImpl(
    private val userService: UserService,
    private val competitionService: CompetitionService,
) : CompetitionFacade {
    override fun deleteById(id: Long): Competition =
        competitionService.deleteById(id)

    override fun delete(competition: Competition): Competition =
        competitionService.delete(competition)

    override fun create(
        principal: Principal,
        competitionDto: CompetitionDto
    ): Competition = competitionService.create(
        user = userService.findByUsernameOrThrow(principal.name),
        name = competitionDto.name,
        dateFrom = competitionDto.dateFrom,
        dateTo = competitionDto.dateTo
    )

    override fun findAllActiveByUser(principal: Principal): List<Competition> =
        competitionService.findAllActiveByUser(
            userService.findByUsernameOrThrow(principal.name)
        )

    override fun hardDelete(competition: Competition) =
        competitionService.hardDelete(competition)

    override fun hardDeleteById(id: Long) =
        competitionService.hardDeleteById(id)

    override fun rename(principal: Principal, id: Long, name: String): Competition =
        competitionService.rename(
            userService.findByUsernameOrThrow(principal.name),
            id,
            name
        )

    override fun getById(id: Long): Competition =
        competitionService.getById(id)

    override fun deleteByIdAndUser(principal: Principal, id: Long): Competition =
        competitionService.deleteByIdAndUser(
            userService.findByUsernameOrThrow(principal.name),
            id
        )
}
