package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.AttemptResult
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.Team

@Repository
interface AttemptResultRepository : JpaRepository<AttemptResult, Long> {
    fun existsByAttemptAndTeam(attempt: Attempt, team: Team): Boolean
    fun getByAttemptAndTeam(attempt: Attempt, team: Team): AttemptResult
    fun getByAttemptAndTeamAndStatusNot(
        attempt: Attempt,
        team: Team,
        status: Status
    ): AttemptResult

    fun getAllByTeam(team: Team): List<AttemptResult>
}
