package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.anarcom.octopus.dto.exception.ExceptionDto
import ru.anarcom.octopus.exceptions.CannotActivateException

@ControllerAdvice
class CannotActivateExceptionHandler : AbstractExceptionHandler<CannotActivateException>() {
    @ExceptionHandler(
        value = [
            CannotActivateException::class,
        ]
    )
    override fun handleException(
        exception: CannotActivateException,
        request: WebRequest
    ) = ResponseEntity.status(HttpStatus.CONFLICT)
        .body(
            ExceptionDto(
            humanMessage = exception.message ?: "",
            exceptionMessage = exception.message?: "",
        ))
}