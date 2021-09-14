package ru.anarcom.octopus.controller.validation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.anarcom.octopus.dto.validation.EmailValidationDto
import ru.anarcom.octopus.dto.validation.UsernameValidationDto
import ru.anarcom.octopus.dto.validation.ValidationMessageDto
import ru.anarcom.octopus.service.UserService

@RestController
@RequestMapping("api/v1/registration/validation")
class RegistrationValidationController(
    private val userService: UserService
) {
    @PostMapping("username")
    fun validateByUsername(
        @RequestBody usernameValidationDto: UsernameValidationDto
    ) = if (!userService.existsByUsername(usernameValidationDto.username)) {
        ValidationMessageDto(OK_MESSAGE)
    } else {
        ValidationMessageDto(ALREADY_USED_MESSAGE)
    }

    @PostMapping("email")
    fun validateByEmail(
        @RequestBody emailValidationDto: EmailValidationDto
    ) = if (userService.existsByEmail(emailValidationDto.email)) {
        ValidationMessageDto(OK_MESSAGE)
    } else {
        ValidationMessageDto(ALREADY_USED_MESSAGE)
    }

    companion object {
        const val OK_MESSAGE = "ok"
        const val ALREADY_USED_MESSAGE = "already used"
    }
}
