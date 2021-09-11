package ru.anarcom.octopus.exceptions.handlers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
import org.springframework.web.servlet.view.json.MappingJackson2JsonView
import ru.anarcom.octopus.util.logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Custom Exception handler. Returns json with two params
 * 1. Human_message for message
 * 2. Excepion_message with Exception::message
 */
@Component
class CustomExceptionHandler(
    val customExceptionToRespConverters: List<CustomExceptionToRespConverter>
) : AbstractHandlerExceptionResolver() {
    private val logger = logger()

    override fun doResolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView? {
        val exceptionHandler = customExceptionToRespConverters
            .filter {
                it.isThisHandler(ex)
            }
        val modelAndView = ModelAndView(MappingJackson2JsonView())
        // if == 0 there are no handlers for this exception
        // if > 1 there are more than 1 handler
        if (exceptionHandler.size != 1) {
            modelAndView.status = HttpStatus.INTERNAL_SERVER_ERROR
            modelAndView.addObject("human_message", "Unknown Exception.")
        } else {
            modelAndView.status = exceptionHandler[0].getHttpCode()
            modelAndView.addObject("human_message", exceptionHandler[0].getHumanMessage())
        }
        logger.error("Exception handled ${ex.message}", ex)
        modelAndView.addObject("exception_message", ex.message)
        return modelAndView
    }
}
