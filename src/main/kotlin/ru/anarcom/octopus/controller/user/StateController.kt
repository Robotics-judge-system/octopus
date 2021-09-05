package ru.anarcom.octopus.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StateController {

    @GetMapping("/api/state/ping")
    fun ping() = "pong"
}