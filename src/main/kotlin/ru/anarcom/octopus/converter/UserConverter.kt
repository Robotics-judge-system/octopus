package ru.anarcom.octopus.converter

import ru.anarcom.octopus.dto.UserDto
import ru.anarcom.octopus.entity.User
import java.util.*

class UserConverter {
    companion object {
        fun toDto(user: User): UserDto =
            UserDto(
                user.id ?: -1,
                user.email ?: "empty email field",
                user.lastOnlineAt ?: Date()
            )
    }
}