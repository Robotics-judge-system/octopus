package ru.anarcom.octopus.service

import org.springframework.beans.factory.annotation.Autowired
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.repository.UserRepository

class UserServiceTest:TestWithDb() {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun contextLoads() {
    }
}