package com.epam.learn.microservices.fundamentals.resource.service.controller.error

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
    fun handleDefault(exception: Exception?, locale: Locale): ErrorResponse {
        val errorCode = ErrorCode.UNCATEGORIZED
        val message = messages.getMessage(errorCode.label, null, locale)
        val timestamp = LocalDateTime.now()

        return ErrorResponse(errorCode, message, timestamp)
    }
}
