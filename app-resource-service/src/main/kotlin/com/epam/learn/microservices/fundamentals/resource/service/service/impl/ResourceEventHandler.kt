package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceBinaryDataNotFoundEvent
import org.springframework.stereotype.Component

@Component
class ResourceEventHandler {

    fun handleResourceBinaryDataNotFoundEvent(event: ResourceBinaryDataNotFoundEvent) {

    }
}
