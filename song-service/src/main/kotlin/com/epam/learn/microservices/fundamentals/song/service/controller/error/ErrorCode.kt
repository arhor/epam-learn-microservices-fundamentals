package com.epam.learn.microservices.fundamentals.song.service.controller.error

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = ErrorCodeSerializer::class)
enum class ErrorCode(val type: Type, val numericValue: Int, val label: String) {
    // @formatter:off
    UNCATEGORIZED      (Type.GEN, 0, "error.server.internal"),

    RESOURCE_DUPLICATE (Type.DAT, 1, "error.resource.duplicate"),
    RESOURCE_NOT_FOUND (Type.DAT, 2, "error.resource.not.found"),

    VALIDATION_FAILED  (Type.VAL, 3, "error.validation.failed"),
    // @formatter:on
    ;

    enum class Type {
        GEN,
        DAT,
        VAL,
        ;
    }
}
