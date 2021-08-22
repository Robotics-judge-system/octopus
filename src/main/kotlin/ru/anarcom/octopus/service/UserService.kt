package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.User

interface UserService {
    fun getUserByUsername(name: String): User

    fun getUserByEmail(email: String): User

    fun registerUser(name: String, email: String, password:String): User

    fun activateUser(email: String): User
}