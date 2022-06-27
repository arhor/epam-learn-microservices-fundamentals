package com.epam.learn.microservices.fundamentals.resource.service.service

data class ResourceEvent(val type: Type, val id: Long) {

    enum class Type {
        CREATED, DELETED
    }
}
