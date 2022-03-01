package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Attempt

interface AttemptService {
    fun save(attempt: Attempt): Attempt
    fun saveNew(attempt: Attempt): Attempt
}
