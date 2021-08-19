package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.entity.User

interface UserService {
    fun getUserByUsername(name: String): User

    fun getUserByEmail(email: String): User
}