package ru.anarcom.octopus.calculation

import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.FormulaProtocol

@Service
class AttemptResultCalculator {
    fun calculateAttemptResult(
        formulaProtocol: FormulaProtocol,
        attemptData: Map<String, String>
    ): Pair<Long, Long> {
        val score: Long
        val time: Long
        if (attemptData.containsKey("a")) {
            score = attemptData["a"]?.toLong()?.plus(1)!!
            time = 123
        } else {
            score = 12
            time = 124
        }
        return Pair(score, time)
    }
}
