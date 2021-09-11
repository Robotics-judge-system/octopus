package ru.anarcom.octopus.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.anarcom.octopus.dto.AdminUserDto
import ru.anarcom.octopus.service.UserService
import java.security.Principal

@RestController
@RequestMapping("api/v1")
class SelfController(
    private var userService: UserService
) {
    @GetMapping("self")
    fun getSelfInformation(
        principal: Principal,
    ): AdminUserDto {
        return AdminUserDto.fromUser(
            userService.findByUsername(
                principal.name
            )
        )
    }
}