package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEvent
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEventPublisher
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class ResourceEventPublisherImpl(private val jmsTemplate: JmsTemplate) : ResourceEventPublisher {

    @Retry(name = "resource-events")
    override fun publishEvent(event: ResourceEvent<*>) {
        jmsTemplate.convertAndSend(
            objectMapper.writeValueAsBytes(
                event
            )
        )
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
