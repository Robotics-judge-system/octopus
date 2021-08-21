package ru.anarcom.octopus.service

import javassist.NotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.repos.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
): UserService {
    override fun getUserByUsername(name: String): User =
        userRepository.findUserByName(name) ?: throw NotFoundException("User not found")

    override fun getUserByEmail(email: String): User =
        userRepository.findUserByEmail(email) ?: throw NotFoundException("User not found")

    override fun registerUser(name: String, email: String, password: String): User =
        userRepository.save(
            User(
                name = name,
                email = email,
                passwordHash = bCryptPasswordEncoder.encode(password)
            )
        )
}