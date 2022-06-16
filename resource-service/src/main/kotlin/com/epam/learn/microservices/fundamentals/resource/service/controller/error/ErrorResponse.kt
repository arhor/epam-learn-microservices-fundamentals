package com.epam.learn.microservices.fundamentals.resource.service.controller.error

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.io.Serializable
import java.time.LocalDateTime

@JsonPropertyOrder("code", "message", "details", "timestamp")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val code: ErrorCode = ErrorCode.UNCATEGORIZED,
    val message: String,
    val details: List<String>?,
    val timestamp: LocalDateTime,
) : Serializable
