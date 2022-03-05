package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.AttemptResult

interface AttemptResultService {
    fun save(attempt: AttemptResult): AttemptResult
    fun saveNew(attempt: AttemptResult): AttemptResult
}
