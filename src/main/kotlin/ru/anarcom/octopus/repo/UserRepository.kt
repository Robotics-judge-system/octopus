package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(name: String): User?
    fun findByEmail(email: String): User?
}