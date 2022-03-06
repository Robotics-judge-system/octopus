package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.anarcom.octopus.dto.exception.ExceptionDto
import ru.anarcom.octopus.exceptions.UpdateAttemptResultException

@ControllerAdvice
class UpdateAttemptResultExceptionHandler :
    AbstractExceptionHandler<UpdateAttemptResultException>() {
    @ExceptionHandler(
        value = [
            UpdateAttemptResultException::class,
        ]
    )
    override fun handleException(
        exception: UpdateAttemptResultException,
        request: WebRequest
    ) = ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(
            ExceptionDto(
                humanMessage = "update attempt result error",
                exceptionMessage = exception.message,
            )
        )
}
