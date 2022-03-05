package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.Attempt
import ru.anarcom.octopus.entity.Category

interface AttemptService {
    fun save(attempt: Attempt): Attempt
    fun saveNew(attempt: Attempt): Attempt
    fun findAttemptByCategoryAndIdOrThrow(category: Category, Id: Long): Attempt
}
