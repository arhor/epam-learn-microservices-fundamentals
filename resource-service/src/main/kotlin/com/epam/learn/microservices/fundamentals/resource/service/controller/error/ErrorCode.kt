package com.epam.learn.microservices.fundamentals.resource.service.controller.error

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = ErrorCodeSerializer::class)
enum class ErrorCode(val type: Type, val numericValue: Int, val label: String) {
    // @formatter:off
    UNCATEGORIZED    (Type.GEN, 0, "error.server.internal"),

    ENTITY_DUPLICATE (Type.DAT, 1, "error.entity.duplicate"),
    ENTITY_NOT_FOUND (Type.DAT, 2, "error.entity.not.found"),
    // @formatter:on
    ;

    enum class Type {
        GEN,
        DAT,
        ;
    }
}
