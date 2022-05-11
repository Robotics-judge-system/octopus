package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.anarcom.octopus.entity.*

@Repository
interface AttemptResultRepository : JpaRepository<AttemptResult, Long> {
    fun existsByAttemptAndTeam(attempt: Attempt, team: Team): Boolean
    fun existsByAttemptAndTeamAndStatusNot(attempt: Attempt, team: Team, status: Status): Boolean
    fun getByAttemptAndTeam(attempt: Attempt, team: Team): AttemptResult
    fun getByAttemptAndTeamAndStatusNot(
        attempt: Attempt,
        team: Team,
        status: Status
    ): AttemptResult

    fun getAllByTeam(team: Team): List<AttemptResult>
    fun getAllByTeamAndStatusNot(team: Team, status: Status): List<AttemptResult>
    fun countAllByAttemptAndStatus(attempt: Attempt, status: Status): Long
    fun countAllByFormulaProtocolAndStatusNot(
        formulaProtocol: FormulaProtocol,
        status: Status
    ):Long
}
