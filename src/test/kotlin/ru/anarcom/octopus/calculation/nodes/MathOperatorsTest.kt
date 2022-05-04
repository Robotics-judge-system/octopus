package ru.anarcom.octopus.calculation.nodes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.anarcom.octopus.exceptions.CalculationException

class MathOperatorsTest : NodeTest() {

    @ParameterizedTest
    @MethodSource("normalTestSource")
    fun normalWorkTest(path: String, ans: Int) {
        val nodes = getNodes(path)
        val node = nodes.nodes["1"]!!
        val protocolData = HashMap<String, Int>()
        val res = node.calculate(nodes.nodes, protocolData, "num")

        Assertions.assertEquals(ans, res)
    }

    @ParameterizedTest
    @MethodSource("notAvailableOutputSource")
    fun notAvailableOutputTest(path: String, exceptionMessage: String) {
        val nodes = getNodes(path)
        val node = nodes.nodes["1"]!!
        val protocolData = HashMap<String, Int>()

        val ex = Assertions.assertThrows(
            CalculationException::class.java,
        ) {
            node.calculate(nodes.nodes, protocolData, "error_name")
        }

        Assertions.assertEquals("No such field 'error_name'", ex.message)
    }


    companion object {
        @JvmStatic
        fun normalTestSource() = listOf(
            Arguments.of("/calculation/nodes/normal_add_node.json", 3),
            Arguments.of("/calculation/nodes/not_connected_a_add_node.json", 3),
            Arguments.of("/calculation/nodes/not_connected_b_add_node.json", 3),
            Arguments.of("/calculation/nodes/not_connected_add_node.json", 5),

            Arguments.of("/calculation/nodes/normal_minus_node.json", 1),
            Arguments.of("/calculation/nodes/not_connected_a_minus_node.json", 32),
            Arguments.of("/calculation/nodes/not_connected_b_minus_node.json", -2),
            Arguments.of("/calculation/nodes/not_connected_minus_node.json", -10),

            Arguments.of("/calculation/nodes/normal_mult_node.json", 2),
            Arguments.of("/calculation/nodes/not_connected_a_mult_node.json", 19),
            Arguments.of("/calculation/nodes/not_connected_b_mult_node.json", 6),
            Arguments.of("/calculation/nodes/not_connected_mult_node.json", 0),
        )

        @JvmStatic
        fun notAvailableOutputSource() = listOf(
            Arguments.of("/calculation/nodes/normal_add_node.json", ""),
            Arguments.of("/calculation/nodes/normal_minus_node.json", ""),
            Arguments.of("/calculation/nodes/normal_mult_node.json", ""),
        )
    }
}
