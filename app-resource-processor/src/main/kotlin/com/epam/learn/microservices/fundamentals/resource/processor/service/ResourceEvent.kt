package com.epam.learn.microservices.fundamentals.resource.processor.service

data class ResourceEvent(val type: Type, val payload: Any) {

    enum class Type { CREATED, DELETED }
}
