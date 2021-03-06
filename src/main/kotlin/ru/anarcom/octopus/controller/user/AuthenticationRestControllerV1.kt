package ru.anarcom.octopus.controller.user


import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import ru.anarcom.octopus.dto.login.AuthenticationRequestDto
import ru.anarcom.octopus.dto.login.LoginResponseDto
import ru.anarcom.octopus.dto.login.RefreshTokenDto
import ru.anarcom.octopus.dto.user.AuthDto
import ru.anarcom.octopus.exceptions.InvalidLoginOrPasswordException
import ru.anarcom.octopus.security.jwt.JwtTokenProvider
import ru.anarcom.octopus.service.AuthService
import ru.anarcom.octopus.service.UserService
import ru.anarcom.octopus.util.logger
import java.security.Principal


/**
 * REST controller for authentication requests (login)
 *
 */
@RestController
@RequestMapping( "api/v1/auth")
class AuthenticationRestControllerV1(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
    private val authService: AuthService,
) {
    private val log = logger()

    @PostMapping("login")
    fun login(@RequestBody requestDto: AuthenticationRequestDto): ResponseEntity<*> {
        return try {
            val login = requestDto.login
            val user = if (login.contains("@")) {
                userService.findByEmailOrThrow(login)
            } else {
                userService.findActiveByUsername(login)
                    ?: throw InvalidLoginOrPasswordException()
            }
            val pair = jwtTokenProvider.createToken(user.username, user.roles)
            ResponseEntity.ok(
                LoginResponseDto(
                    user.username,
                    pair.component1(),
                    authService.getNewRefreshTokenForUser(user),
                    pair.component2()
                )
            )
        } catch (e: AuthenticationException) {
            throw InvalidLoginOrPasswordException()
        }
    }

    @PostMapping("refresh")
    fun getNewTokenFromRefresh(
        @RequestBody refreshTokenDto: RefreshTokenDto
    ): ResponseEntity<*> {
        return try {
            val token = refreshTokenDto.refresh
            val user = authService.getUserByRefreshToken(token)
            val pair = jwtTokenProvider.createToken(
                user.username,
                user.roles
            )
            ResponseEntity.ok(
                LoginResponseDto(
                    user.username,
                    pair.component1(),
                    token,
                    pair.component2()
                )
            )
        } catch (e: AuthenticationException) {
            throw InvalidLoginOrPasswordException()
        }
    }

    @GetMapping("auth")
    fun getAllAuthsForUser(
        principal: Principal,
    ) = AuthDto.fromInstant(
        authService.getActiveAuthsForUser(
            userService.findByUsernameOrThrow(principal.name)
        )
    )
}
