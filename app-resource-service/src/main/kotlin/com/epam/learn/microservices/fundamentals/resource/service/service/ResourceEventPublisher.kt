package com.epam.learn.microservices.fundamentals.resource.service.service

import com.epam.learn.microservices.fundamentals.event.ResourceEvent

interface ResourceEventPublisher {

    fun publishEvent(event: ResourceEvent)
}
