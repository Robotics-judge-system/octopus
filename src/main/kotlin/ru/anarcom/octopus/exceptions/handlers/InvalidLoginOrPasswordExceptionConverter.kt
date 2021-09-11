package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.anarcom.octopus.exceptions.InvalidLoginOrPasswordException

@Component
class InvalidLoginOrPasswordExceptionConverter:CustomExceptionToRespConverter {
    override fun isThisHandler(e: Exception): Boolean = e is InvalidLoginOrPasswordException

    override fun getHumanMessage(): String = "Login or password is incorrect."

    override fun getHttpCode(): HttpStatus = HttpStatus.FORBIDDEN
}