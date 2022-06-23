package com.epam.learn.microservices.fundamentals.resource.service.service.event

import org.springframework.context.event.EventListener
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class ResourceCreatedEventHandler(private val jmsTemplate: JmsTemplate) {

    @EventListener(ResourceCreatedEvent::class)
    fun publishEvent(event: ResourceCreatedEvent) {
        jmsTemplate.convertAndSend(event)
    }
}
