package ru.anarcom.octopus.service

import javassist.NotFoundException
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.entity.User
import ru.anarcom.octopus.repos.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {
    override fun getUserByUsername(name: String): User =
        userRepository.findUserByName(name)?: throw NotFoundException("User not found")

    override fun getUserByEmail(email: String): User =
        userRepository.findUserByEmail(email)?: throw NotFoundException("User not found")
}