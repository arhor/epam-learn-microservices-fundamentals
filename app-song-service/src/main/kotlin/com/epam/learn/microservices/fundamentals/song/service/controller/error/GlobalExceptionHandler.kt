package com.epam.learn.microservices.fundamentals.song.service.controller.error

import com.epam.learn.microservices.fundamentals.LoggerContext
import com.epam.learn.microservices.fundamentals.error.AbstractGlobalExceptionHandler
import com.epam.learn.microservices.fundamentals.error.ErrorResponse
import com.epam.learn.microservices.fundamentals.song.service.controller.error.ResourceServiceErrorCode.*
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.invoke.MethodHandles
import java.util.*
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler(messages: MessageSource) : AbstractGlobalExceptionHandler(messages) {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityDuplicateException::class)
    fun handleEntityDuplicateException(exception: EntityDuplicateException, locale: Locale): ErrorResponse {
        log.error("Resource with specified condition already exists", exception)
        return createErrorResponse(RESOURCE_DUPLICATE, locale, exception.condition)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(exception: EntityNotFoundException, locale: Locale): ErrorResponse {
        log.error("Resource with specified condition is not found", exception)
        return createErrorResponse(RESOURCE_NOT_FOUND, locale, exception.condition)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(exception: ConstraintViolationException, locale: Locale): ErrorResponse {
        log.error("Validation failed", exception)
        val constraintViolationMessages = exception.constraintViolations.map { it.message }
        return createErrorResponse(VALIDATION_FAILED, locale, details = constraintViolationMessages)
    }

    companion object : LoggerContext {
        override val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
