package ru.anarcom.octopus.service

import com.github.springtestdbunit.annotation.DatabaseSetup
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.repository.UserRepository

class UserServiceTest:TestWithDb() {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    @DatabaseSetup("/db/test.xml")
    fun contextLoads() {
        println(userRepository.getById(1).toString())
    }
}