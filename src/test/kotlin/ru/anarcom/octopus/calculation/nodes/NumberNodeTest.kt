package ru.anarcom.octopus.calculation.nodes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.anarcom.octopus.exceptions.CalculationException

class NumberNodeTest : NodeTest() {

    @Test
    @DisplayName("Get number")
    fun getNumberTest(){
        val nodes = getNodes("calculation/nodes/number_node.json")
        val node = nodes.nodes["1"]!!

        val protocolData = HashMap<String, Int>()

        val res = node.calculate(nodes.nodes, protocolData, "num")
        Assertions.assertEquals(-290, res)
    }

    @Test
    @DisplayName("Mistake in output connection name")
    fun mistakeInOutputConnectionNameTest(){

        val nodes = getNodes("calculation/nodes/number_node.json")
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
