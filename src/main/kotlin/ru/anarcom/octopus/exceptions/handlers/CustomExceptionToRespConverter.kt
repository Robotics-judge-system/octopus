package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus

/**
 * Interface for Exception Handling @see CustomExceptionHandler
 */
interface CustomExceptionToRespConverter {
    fun isThisHandler(e: Exception): Boolean
    fun getHumanMessage():String
    fun getHttpCode(): HttpStatus
}
