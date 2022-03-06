package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.anarcom.octopus.dto.exception.ExceptionDto
import ru.anarcom.octopus.exceptions.InvalidLoginOrPasswordException

@ControllerAdvice
class InvalidLoginOrPasswordExceptionHandler :
    AbstractExceptionHandler<InvalidLoginOrPasswordException>() {
    @ExceptionHandler(
        value = [
            InvalidLoginOrPasswordException::class,
        ]
    )
    override fun handleException(
        exception: InvalidLoginOrPasswordException,
        request: WebRequest
    ): ResponseEntity<ExceptionDto> =
        ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                ExceptionDto(
                    humanMessage = "login or password incorrect",
                    exceptionMessage = exception.message ?: ""
                )
            )
}
