package com.epam.learn.microservices.fundamentals.resource.service.service.exception

abstract class PropertyConditionException(
    /**
     * Property condition caused the exception. For example: id = 0.
     */
    val condition: String,
) : RuntimeException()
