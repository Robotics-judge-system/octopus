package ru.anarcom.octopus.controller.user


import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import ru.anarcom.octopus.security.jwt.JwtTokenProvider
import ru.anarcom.octopus.service.UserService
import ru.anarcom.octopus.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import ru.anarcom.octopus.dto.login.AuthenticationRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.RequestBody
import ru.anarcom.octopus.dto.login.LoginResponseDto
import ru.anarcom.octopus.exceptions.InvalidLoginOrPasswordException
import ru.anarcom.octopus.dto.login.RefreshTokenDto

/**
 * REST controller for authentication requests (login)
 *
 */
@RestController
@RequestMapping(value = ["/api/v1/auth/"])
class AuthenticationRestControllerV1(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
    private val authService: AuthService,
) {

    @PostMapping("login")
    fun login(@RequestBody requestDto: AuthenticationRequestDto): ResponseEntity<*> {
        return try {
            val username = requestDto.username
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, requestDto.password))
            val user = userService.findByUsername(username)
                ?: throw UsernameNotFoundException("User with username: $username not found")
            val pair = jwtTokenProvider.createToken(username, user.roles)
            ResponseEntity.ok(
                LoginResponseDto(
                    username,
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
                user!!.username,
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
}
