package ru.anarcom.octopus.security.jwt

import org.springframework.web.filter.GenericFilterBean
import kotlin.Throws
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder

/**
 * JWT token filter that handles all HTTP requests to application.
 *
 */
class JwtTokenFilter(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {
        val token = jwtTokenProvider.resolveToken(req as HttpServletRequest)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val auth = jwtTokenProvider.getAuthentication(token)
            if (auth != null) {
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        filterChain.doFilter(req, res)
    }
}