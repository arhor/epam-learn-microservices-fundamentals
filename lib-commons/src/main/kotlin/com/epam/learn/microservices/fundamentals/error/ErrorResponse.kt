package com.epam.learn.microservices.fundamentals.error

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.io.Serializable
import java.time.temporal.Temporal

@JsonPropertyOrder("code", "message", "details", "timestamp")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val code: ErrorCode,
    val message: String,
    val timestamp: Temporal,
    val details: List<String>? = null,
) : Serializable
