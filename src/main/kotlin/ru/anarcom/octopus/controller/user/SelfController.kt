package ru.anarcom.octopus.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.anarcom.octopus.dto.UserDto
import ru.anarcom.octopus.security.jwt.JwtTokenProvider
import ru.anarcom.octopus.service.UserService

@RestController
@RequestMapping("api/v1")
class SelfController(
    private var jwtTokenProvider: JwtTokenProvider,
    private var userService: UserService
) {
    @GetMapping("self")
    fun getSelfInformation(
        @RequestHeader(name = "Authorization") token: String
    ): UserDto =
        UserDto.fromUser(
            userService.findByUsername(
                jwtTokenProvider.getUsernameFromJwtToken(
                    jwtTokenProvider.getBodyOfHeaderToken(token)
                )
            )
        )
}