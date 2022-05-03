package ru.anarcom.octopus.calculation.nodes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.anarcom.octopus.calculaion.interpreter.node.IfNode
import ru.anarcom.octopus.exceptions.CalculationException

class IfStatementNodeTest : NodeTest() {

    @ParameterizedTest
    @MethodSource("normalTestSource")
    fun normalWorkTest(path: String, comparator: String, ans: Int) {
        val nodes = getNodes(path)
        val node = nodes.nodes["1"]!! as IfNode
        val protocolData = HashMap<String, Int>()

        node.data.textCond = comparator

        val res = node.calculate(nodes.nodes, protocolData, "result")
        Assertions.assertEquals(ans, res)

    }

    @Test
    @DisplayName("mistake in wire name")
    fun mistakeOutputNameTest() {
        val nodes = getNodes("/calculation/nodes/if_statement_node_a.json")
        val node = nodes.nodes["1"]!! as IfNode
        val protocolData = HashMap<String, Int>()

        val ex = Assertions.assertThrows(
            CalculationException::class.java
        ) {
            node.calculate(nodes.nodes, protocolData, "error_name")
        }

        Assertions.assertEquals("No such field 'error_name'", ex.message)

    }

    @Test
    @DisplayName("mistake in comparator name")
    fun mistakeInComparatorTest() {
        val nodes = getNodes("/calculation/nodes/if_statement_node_mistake.json")
        val node = nodes.nodes["1"]!!
        val protocolData = HashMap<String, Int>()

        val ex = Assertions.assertThrows(
            CalculationException::class.java
        ) {
            node.calculate(nodes.nodes, protocolData, "result")
        }

        Assertions.assertEquals("No such comparator '<**'", ex.message)
    }

    companion object {
        @JvmStatic
        fun normalTestSource() = listOf(
            Arguments.of("/calculation/nodes/if_statement_node_a.json", "<", 3),
            Arguments.of("/calculation/nodes/if_statement_node_b.json", "<", 4),
            Arguments.of("/calculation/nodes/if_statement_node_a.json", "<=", 3),
            Arguments.of("/calculation/nodes/if_statement_node_b.json", "<=", 4),
            Arguments.of("/calculation/nodes/if_statement_node_a.json", "==", 4),
            Arguments.of("/calculation/nodes/if_statement_node_b.json", "==", 4)
        )
    }
}
