package com.epam.learn.microservices.fundamentals.resource.processor.service

sealed interface ResourceEvent {

    data class Created(val id: Long) : ResourceEvent

    data class Deleted(val ids: List<Long>) : ResourceEvent
}
