package ru.anarcom.octopus.calculation.nodes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.anarcom.octopus.exceptions.CalculationException

class InputNodeTest : NodeTest() {

    @Test
    @DisplayName("Key exists in protocol map")
    fun keyExistsInMapTest() {
        val nodes = getNodes("calculation/nodes/input_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()
        protocolData["Blankets"] = 10

        val res = node.calculate(nodes.nodes, protocolData, "val")
        Assertions.assertEquals(10, res)
    }

    @Test
    @DisplayName("Key does not exists in protocol map")
    fun keyDoesNotExistsInMapTest() {
        val nodes = getNodes("calculation/nodes/input_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()

        Assertions.assertThrows(
            CalculationException::class.java,
            {
                node.calculate(nodes.nodes, protocolData, "val")
            },
            "Key 'Blankets' not found"
        )
    }

    @Test
    @DisplayName("Mistake in output node name")
    fun errorInOutputValueNameTest() {
        val nodes = getNodes("calculation/nodes/input_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()

        Assertions.assertThrows(
            CalculationException::class.java,
            {
                node.calculate(nodes.nodes, protocolData, "error_name")
            },
            "No such field 'error_name'"
        )
    }
}
