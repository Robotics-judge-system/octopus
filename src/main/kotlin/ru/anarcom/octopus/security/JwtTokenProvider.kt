package ru.anarcom.octopus.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.lang3.time.DateUtils.addHours
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ru.anarcom.octopus.service.UserService
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider {
    @Value("\${jwt.token.secret}")
    private lateinit var secret: String

    @Value("\${jwt.token.expired}")
    private var validInHours: Long = 1

    private lateinit var userService: UserService

    @PostConstruct
    private fun postConstructor() {
        secret = Base64.getEncoder().encodeToString(secret.toByteArray())
    }

    fun createToken(email: String): String {
        val claims = Jwts.claims()
        claims["email"] = email
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(addHours(Date(), validInHours.toInt()))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    fun getAuthentication(token: String): Authentication {
        val user = userService.getUserByEmail(
            getUsername(token).orEmpty()
        )
        val details = JwtUser(
            password = user.passwordHash.orEmpty(),
            username = user.email.orEmpty()
        )
        return UsernamePasswordAuthenticationToken(details, "", details.authorities)
    }

    fun getUsername(token: String?): String? {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body["email"] as String
    }

    @Throws(JwtAuthException::class)
    fun validateToken(token: String?): Boolean {
        return try {
            val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: JwtException) {
            throw JwtAuthException("JWT token is expired or invalid")
        } catch (e: IllegalArgumentException) {
            throw JwtAuthException("JWT token is expired or invalid")
        }
    }

    fun resolveToken(req: HttpServletRequest): String {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else throw JwtAuthException("Http Headers invalid")
    }
}