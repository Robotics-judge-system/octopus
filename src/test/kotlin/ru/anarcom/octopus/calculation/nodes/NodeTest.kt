package ru.anarcom.octopus.calculation.nodes

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import ru.anarcom.octopus.calculaion.interpreter.env.NodeGroup
import ru.anarcom.octopus.utilus.ResourceReader

open class NodeTest {
    fun getNodes(filePath: String): NodeGroup = ObjectMapper().readValue(
        ResourceReader.getResource(filePath),
        NodeGroup::class.java
    )!!
}
