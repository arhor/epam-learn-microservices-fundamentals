package com.epam.learn.microservices.fundamentals.resource.service.service

sealed interface ResourceEvent<T> {

    val type: Type
    val payload: T

    enum class Type { CREATED, DELETED }

    data class Created(override val payload: Long) : ResourceEvent<Long> {
        override val type: Type
            get() = Type.CREATED
    }

    data class Deleted(override val payload: List<Long>) : ResourceEvent<List<Long>> {
        override val type: Type
            get() = Type.DELETED
    }
}
