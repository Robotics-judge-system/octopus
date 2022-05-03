package ru.anarcom.octopus.calculation.nodes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.anarcom.octopus.exceptions.CalculationException

class OutputNodeTest : NodeTest() {
    @Test
    @DisplayName("normal work test")
    fun normalWorkTest() {
        val nodes = getNodes("calculation/nodes/normal_output_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()

        val time = node.calculate(nodes.nodes, protocolData, "time")
        val res = node.calculate(nodes.nodes, protocolData, "res")

        Assertions.assertEquals(1, res)
        Assertions.assertEquals(2, time)
    }

    @Test
    @DisplayName("time not connected")
    fun timeNotConnectedTest() {
        val nodes = getNodes("calculation/nodes/time_not_connected_output_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()

        Assertions.assertThrows(
            CalculationException::class.java,
            {
                node.calculate(nodes.nodes, protocolData, "time")
            },
            "'time' cannot be computed"
        )

        Assertions.assertEquals(1, node.calculate(nodes.nodes, protocolData, "res"))
    }

    @Test
    @DisplayName("score not connected")
    fun scoreNotConnectedTest(){
        val nodes = getNodes("calculation/nodes/score_not_connected_output_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()

            Assertions.assertThrows(
                CalculationException::class.java,
                {
                    node.calculate(nodes.nodes, protocolData, "res")
                },
                "'res' cannot be computed"
            )


        Assertions.assertEquals(2, node.calculate(nodes.nodes, protocolData, "time"))
    }

    @Test
    @DisplayName("mistake in key")
    fun keyErrorTest(){
        val nodes = getNodes("calculation/nodes/normal_output_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()

        val ex = Assertions.assertThrows(
            CalculationException::class.java
        ) {
            node.calculate(nodes.nodes, protocolData, "error_name")
        }

        Assertions.assertEquals("No such key 'error_name'", ex.message)
    }
}