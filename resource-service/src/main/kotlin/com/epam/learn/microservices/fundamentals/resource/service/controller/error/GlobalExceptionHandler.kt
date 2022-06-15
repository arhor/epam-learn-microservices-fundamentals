package com.epam.learn.microservices.fundamentals.resource.service.controller.error

import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler(private val messages: MessageSource) {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleDefault(exception: Exception, locale: Locale): ErrorResponse {
        log.error("An error occurred, consider specify a separate exception handler for it", exception)
        return createErrorResponse(ErrorCode.UNCATEGORIZED, locale)
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityDuplicateException::class)
    fun handleEntityDuplicateException(exception: EntityDuplicateException, locale: Locale): ErrorResponse {
        log.error("Entity with specified condition already exists", exception)
        return createErrorResponse(ErrorCode.ENTITY_DUPLICATE, locale, exception.condition)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(exception: EntityNotFoundException, locale: Locale): ErrorResponse {
        log.error("Entity with specified condition is not found", exception)
        return createErrorResponse(ErrorCode.ENTITY_NOT_FOUND, locale, exception.condition)
    }

    private fun createErrorResponse(errorCode: ErrorCode, locale: Locale, vararg args: Any): ErrorResponse {
        val message = messages.getMessage(errorCode.label, args, locale)
        val timestamp = LocalDateTime.now()

        return ErrorResponse(errorCode, message, timestamp)
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}
