package ru.anarcom.octopus.calculation.nodes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.anarcom.octopus.exceptions.CalculationException

class DivideNodeTest : NodeTest() {

    @ParameterizedTest
    @MethodSource("normalWorkTestSource")
    fun normalWorkTest(path: String, rounded: Int, base: Int, reminder: Int) {
        val nodes = getNodes(path)
        val node = nodes.nodes["1"]!!
        val protocolData = HashMap<String, Int>()

        val map = mapOf(
            Pair("out1", rounded),
            Pair("out2", base),
            Pair("out3", reminder),
        )

        map.forEach {
            val res = node.calculate(nodes.nodes, protocolData, it.key)
            Assertions.assertEquals(it.value, res)
        }
    }

    @Test
    @DisplayName("mistake in wire name")
    fun mistakeOutputNameTest() {
        val nodes = getNodes("/calculation/nodes/normal_divide_node.json")
        val node = nodes.nodes["1"]!!
        val protocolData = HashMap<String, Int>()

        val ex = Assertions.assertThrows(
            CalculationException::class.java
        ) {
            node.calculate(nodes.nodes, protocolData, "error_name")
        }

        Assertions.assertEquals("No such field 'error_name'", ex.message)
    }

    @Test
    @DisplayName("Divide by zero")
    fun divideByZero() {
        val nodes = getNodes("/calculation/nodes/by_zero_divide_node.json")
        val node = nodes.nodes["1"]!!
        val protocolData = HashMap<String, Int>()

        val values = listOf(
            "out1",
            "out2",
            "out3",
        )


        values.forEach {
            val ex = Assertions.assertThrows(
                CalculationException::class.java
            ) {
                node.calculate(nodes.nodes, protocolData, it)
            }
            Assertions.assertEquals("B is zero", ex.message)
        }

    }


    companion object {
        @JvmStatic
        fun normalWorkTestSource() = listOf(
            Arguments.of("/calculation/nodes/normal_divide_node.json", 3, 3, 2),
            Arguments.of("/calculation/nodes/not_connected_a_divide_node.json", 4, 3, 3),
            Arguments.of("/calculation/nodes/not_connected_b_divide_node.json", 3, 2, 5),
            Arguments.of("/calculation/nodes/not_connected_divide_node.json", 1, 0, 16),
        )
    }
}