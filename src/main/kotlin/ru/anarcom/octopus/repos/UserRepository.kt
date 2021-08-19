package ru.anarcom.octopus.repos

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findUserByEmail(email: String): User?
    fun findUserByName(name: String): User?
}