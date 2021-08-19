package ru.anarcom.octopus.dto

import java.util.*

data class UserDto(
    val id:Long,
    val email:String,
    val lastOnlineAt: Date
)
