package com.epam.learn.microservices.fundamentals.song.service.controller.error

import com.epam.learn.microservices.fundamentals.error.ErrorCode
import com.epam.learn.microservices.fundamentals.error.ErrorCode.Type

enum class ResourceServiceErrorCode(
    override val type: Type,
    override val value: Int,
    override val label: String,
) : ErrorCode {
    // @formatter:off
    RESOURCE_DUPLICATE (Type.DAT, 1, "error.resource.duplicate"),
    RESOURCE_NOT_FOUND (Type.DAT, 2, "error.resource.not.found"),
    VALIDATION_FAILED  (Type.VAL, 3, "error.validation.failed"),
    // @formatter:on
    ;
}
