package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.anarcom.octopus.dto.exception.ExceptionDto

abstract class AbstractExceptionHandler<T> : ResponseEntityExceptionHandler() {
    protected abstract fun handleException(
        exception: T,
        request: WebRequest,
    ): ResponseEntity<ExceptionDto>
}
