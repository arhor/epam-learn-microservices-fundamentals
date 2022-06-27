package com.epam.learn.microservices.fundamentals.resource.service.service

interface ResourceEventPublisher {

    fun resourceCreated(id: Long)

    fun publishEvent(event: ResourceEvent)
}
