package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.anarcom.octopus.exceptions.IncorrectPasswordException

@Component
class IncorrectPasswordExceptionConverter:CustomExceptionToRespConverter {
    override fun isThisHandler(e: Exception): Boolean = e is IncorrectPasswordException

    override fun getHumanMessage(): String = "Something wrong with password."

    override fun getHttpCode(): HttpStatus = HttpStatus.FORBIDDEN

}