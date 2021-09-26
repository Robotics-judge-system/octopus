package ru.anarcom.octopus.controller.user

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.anarcom.octopus.dto.user.ChangeUserInfoDto
import ru.anarcom.octopus.dto.user.ChangeUserPasswordDto
import ru.anarcom.octopus.dto.user.UserDto
import ru.anarcom.octopus.service.UserService
import ru.anarcom.octopus.util.logger
import java.security.Principal

@RestController
@RequestMapping("api/v1/user/change")
class ChangeUserDataController(
    private val userService: UserService,
) {
    val logger = logger()

    @PostMapping("data")
    fun changeUserData(
        principal: Principal,
        @RequestBody changeUserInfoDto: ChangeUserInfoDto
    ): UserDto = UserDto.fromUser(
        userService.updateUser(
            changeUserInfoDto.name,
            userService.findByUsernameOrThrow(
                principal.name
            )
        )
    ).apply {
        logger.info("User {username = ${principal.name}} changed name data to " +
                "{${changeUserInfoDto.name}}")
    }

    @PostMapping("password")
    fun changeUserPassword(
        principal: Principal,
        @RequestBody changeUserPasswordDto: ChangeUserPasswordDto
    ): UserDto = UserDto.fromUser(
        userService.changePassword(
            userService.findByUsernameOrThrow(
                principal.name
            ),
            changeUserPasswordDto.oldPassword,
            changeUserPasswordDto.newPassword
        )
    ).apply {
        logger.info("User {username = ${principal.name}} changed password")
    }
}
