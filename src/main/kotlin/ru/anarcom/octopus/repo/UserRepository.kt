package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(name: String): User?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?

    fun findByUsernameAndStatus(name: String, status:Status): User?
}
