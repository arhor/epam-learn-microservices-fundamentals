package com.epam.learn.microservices.fundamentals.resource.service.controller.error

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = ErrorCodeSerializer::class)
enum class ErrorCode(val type: Type, val numericValue: Int, val label: String) {
    UNCATEGORIZED(Type.GEN, 0, "error.server.internal"),
    ;

    enum class Type {
        GEN,
        ;
    }
}
