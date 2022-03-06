package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.anarcom.octopus.dto.exception.ExceptionDto
import ru.anarcom.octopus.exceptions.NotFoundException

@ControllerAdvice
class NotFoundExceptionHandler : AbstractExceptionHandler<NotFoundException>() {
    @ExceptionHandler(
        value = [
            NotFoundException::class
        ]
    )
    override fun handleException(
        exception: NotFoundException,
        request: WebRequest
    ) = ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            ExceptionDto(
                humanMessage = "not found",
                exceptionMessage = exception.message ?: "",
            )
        )
}