package com.epam.learn.microservices.fundamentals.event

sealed interface ResourceEvent {

    data class Created(val id: Long) : ResourceEvent

    data class Handled(val id: Long) : ResourceEvent

    data class Deleted(val ids: List<Long>) : ResourceEvent
}
