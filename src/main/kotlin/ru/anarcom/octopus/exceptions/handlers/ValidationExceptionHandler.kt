package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.anarcom.octopus.dto.exception.ExceptionDto
import ru.anarcom.octopus.exceptions.ValidationException

@ControllerAdvice
class ValidationExceptionHandler : AbstractExceptionHandler<ValidationException>() {

    @ExceptionHandler(
        value = [
            ValidationException::class
        ]
    )
    override fun handleException(
        exception: ValidationException,
        request: WebRequest
    ): ResponseEntity<ExceptionDto> = ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(
            ExceptionDto(
                humanMessage = exception.message ?: "no message",
                exceptionMessage = exception.message ?: "no message",
            )
        )
}
