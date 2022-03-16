package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.anarcom.octopus.dto.exception.ExceptionDto
import ru.anarcom.octopus.exceptions.BaseException
import ru.anarcom.octopus.util.logger

@ControllerAdvice
class DefaultExceptionHandler {

    private val logger = logger()

    @ExceptionHandler(
        value = [
            Exception::class,
            RuntimeException::class
        ]
    )
    fun handleException(
        exception: Exception,
        request: WebRequest
    ): ResponseEntity<ExceptionDto> {
        val httpStatus = if (exception is BaseException) {
            exception.httpStatus
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR
        }

        logger.warn("exception handled", exception)

        return ResponseEntity
            .status(httpStatus)
            .body(
                ExceptionDto(
                    exceptionMessage = exception.message ?: "exception text is null",
                )
            )
    }
}
