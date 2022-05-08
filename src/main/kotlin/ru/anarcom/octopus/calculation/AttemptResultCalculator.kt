package ru.anarcom.octopus.calculation

import org.springframework.stereotype.Service
import ru.anarcom.octopus.calculaion.interpreter.node.OutputNode
import ru.anarcom.octopus.entity.FormulaProtocol

@Service
class AttemptResultCalculator {
    fun calculateAttemptResult(
        formulaProtocol: FormulaProtocol,
        attemptData: Map<String, Int>
    ): Pair<Long, Long> {
        val outputNodeId = formulaProtocol.formulaDescription.nodes
            .keys
            .first {
                formulaProtocol.formulaDescription.nodes[it] is OutputNode
            }!!

        val score: Int = formulaProtocol.formulaDescription.nodes[outputNodeId]
            ?.calculate(
                formulaProtocol.formulaDescription.nodes,
                attemptData,
                "res"
                )!! // TODO delete after converting nodes to kotlin

        val time :Int = formulaProtocol.formulaDescription.nodes[outputNodeId]
            ?.calculate(
                formulaProtocol.formulaDescription.nodes,
                attemptData,
                "time"
            )!! // TODO delete after converting nodes to kotlin

        return Pair(score.toLong(), time.toLong())
    }
}
