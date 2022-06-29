package com.epam.learn.microservices.fundamentals.resource.service.service

sealed interface ResourceEvent {

    data class Created(val id: Long) : ResourceEvent

    data class Deleted(val ids: List<Long>) : ResourceEvent
}
