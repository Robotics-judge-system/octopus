package ru.anarcom.octopus.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtTokenFilter(
    private var jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {

    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {
        try {
            val token = jwtTokenProvider.resolveToken((request as HttpServletRequest?)!!)
            if (jwtTokenProvider.validateToken(token)) {
                val auth: Authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
        } catch (e: Exception) {
            val req = request as HttpServletRequest
            val res = response as HttpServletResponse
            res.status = 401
            response.setContentType("application/json")
            val writer = res.writer
            writer.print("something wrong with token")
            writer.flush()
            return
        }
        chain?.doFilter(request, response)
    }
}