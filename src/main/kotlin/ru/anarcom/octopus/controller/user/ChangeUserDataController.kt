package ru.anarcom.octopus.controller.user

import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.AdminUserDto
import ru.anarcom.octopus.dto.ChangeUserInfoDto
import ru.anarcom.octopus.dto.ChangeUserPasswordDto
import ru.anarcom.octopus.security.jwt.JwtTokenProvider
import ru.anarcom.octopus.service.UserService
import ru.anarcom.octopus.util.logger

@RestController
@RequestMapping("api/v1/user/change")
class ChangeUserDataController(
    private val userService: UserService,
    private var jwtTokenProvider: JwtTokenProvider,
) {

    val logger = logger()

    @PostMapping("data")
    fun changeUserData(
        @RequestHeader(name = "Authorization") token: String,
        @RequestBody changeUserInfoDto: ChangeUserInfoDto
    ): AdminUserDto {
        val user = userService.findByUsername(
            jwtTokenProvider.getUsernameFromJwtToken(
                jwtTokenProvider.getBodyOfHeaderToken(token)
            )
        )
        return AdminUserDto.fromUser(
            userService.updateUser(
                changeUserInfoDto.name,
                user
            )
        )
    }

    @PostMapping("password")
    fun changeUserPassword(
        @RequestHeader(name = "Authorization") token: String,
        @RequestBody changeUserPasswordDto: ChangeUserPasswordDto
    ) = AdminUserDto.fromUser(
        userService.changePassword(
            userService.findByUsername(
                jwtTokenProvider.getUsernameFromJwtToken(
                    jwtTokenProvider.getBodyOfHeaderToken(token)
                )
            ),
            changeUserPasswordDto.oldPassword,
            changeUserPasswordDto.newPassword
        )
    )

}