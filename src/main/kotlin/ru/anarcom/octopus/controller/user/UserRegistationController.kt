package ru.anarcom.octopus.controller.user

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.anarcom.octopus.dto.user.RegistrationUserDto
import ru.anarcom.octopus.dto.user.UserDto
import ru.anarcom.octopus.service.UserService

@RestController
@RequestMapping("api/v1")
class UserRegistationController(
    val userService: UserService
) {
    @PostMapping("register")
    fun registerUser(
        @RequestBody registrationUserDto: RegistrationUserDto
    ) = UserDto.fromUser(
        userService.registerUser(
            registrationUserDto.username,
            registrationUserDto.email,
            registrationUserDto.name,
            registrationUserDto.password
        )
    )
}
