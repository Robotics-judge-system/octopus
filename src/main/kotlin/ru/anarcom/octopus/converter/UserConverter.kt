package ru.anarcom.octopus.converter

import ru.anarcom.octopus.dto.UserDto
import ru.anarcom.octopus.entity.entity.User

class UserConverter {
    companion object {
        fun toDto(user: User): UserDto =
            UserDto(
                user.id,
                user.email,
                user.lastOnlineAt
            )
    }
}