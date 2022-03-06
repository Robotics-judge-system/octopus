package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.anarcom.octopus.dto.exception.ExceptionDto
import ru.anarcom.octopus.exceptions.IncorrectPasswordException

@ControllerAdvice
class IncorrectPasswordExceptionHandler : AbstractExceptionHandler<IncorrectPasswordException>() {
    @ExceptionHandler(
        value = [
            IncorrectPasswordException::class,
        ]
    )
    override fun handleException(
        exception: IncorrectPasswordException,
        request: WebRequest
    ) = ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            ExceptionDto(
                humanMessage = "something wrong with password",
                exceptionMessage = exception.message ?: "",
            )
        )
}
