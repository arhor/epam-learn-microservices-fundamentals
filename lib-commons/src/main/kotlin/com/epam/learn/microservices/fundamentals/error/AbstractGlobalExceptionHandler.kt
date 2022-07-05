package com.epam.learn.microservices.fundamentals.error

import com.epam.learn.microservices.fundamentals.LoggerContext
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.util.*

context (LoggerContext)
abstract class AbstractGlobalExceptionHandler(private val messages: MessageSource) {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun handleDefault(exception: Exception): ErrorResponse {
        log.error("An error occurred, consider specify a separate exception handler for it", exception)
        return ErrorResponse(
            code = ErrorCode.UNCATEGORIZED,
            message = "Internal Server Error. Please, contact system administrator.",
            timestamp = currentTimestamp()
        )
    }

    protected fun createErrorResponse(
        errorCode: ErrorCode,
        locale: Locale,
        vararg args: Any,
        details: List<String>? = null
    ): ErrorResponse {

        val message = messages.getMessage(errorCode.label, args, locale)
        val timestamp = currentTimestamp()

        return ErrorResponse(code = errorCode, message = message, details = details, timestamp = timestamp)
    }

    private fun currentTimestamp(): Temporal {
        return ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS)
    }
}
