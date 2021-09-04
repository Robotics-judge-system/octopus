package ru.anarcom.octopus.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.anarcom.octopus.dto.RegistrationUserDto
import ru.anarcom.octopus.dto.UserDto
import ru.anarcom.octopus.service.UserService

@RestController
@RequestMapping("api/v1")
class UserRegistationController(
    val userService: UserService
) {
    @GetMapping("register")
    fun registerUser(
        @RequestBody registrationUserDto: RegistrationUserDto
    ): UserDto = UserDto.fromUser(
        userService.registerUser(
            registrationUserDto.username,
            registrationUserDto.email,
            registrationUserDto.name,
            registrationUserDto.password
        )
    )
}
