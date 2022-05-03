package ru.anarcom.octopus.calculation

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import ru.anarcom.octopus.calculaion.interpreter.env.NodeGroup
import ru.anarcom.octopus.utilus.ResourceReader

class JsonDataSafetyTest {

    @Test
    @DisplayName("check for data saving")
    fun dataSaving() {
        val json = ResourceReader.getResource("/calculation/all_nodes.json")
        val nodes = ObjectMapper().readValue(json, NodeGroup::class.java)
        val newJson = ObjectMapper().writeValueAsString(nodes)
        JSONAssert.assertEquals(json, newJson, true)
    }

}
