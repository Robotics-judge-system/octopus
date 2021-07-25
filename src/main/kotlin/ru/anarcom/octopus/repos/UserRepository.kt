package ru.anarcom.octopus.repos

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.User

interface UserRepository : JpaRepository<User, Long>