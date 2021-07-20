package ru.anarcom.octopus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OctopusApplication

fun main(args: Array<String>) {
	runApplication<OctopusApplication>(*args)
}
