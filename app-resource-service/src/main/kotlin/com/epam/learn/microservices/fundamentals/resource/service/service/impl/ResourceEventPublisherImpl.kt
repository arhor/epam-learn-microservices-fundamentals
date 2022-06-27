package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEventPublisher
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class ResourceEventPublisherImpl(private val jmsTemplate: JmsTemplate) : ResourceEventPublisher {

    @Retry(name = "resource-created")
    override fun resourceCreated(id: Long) {
        jmsTemplate.convertAndSend(id)
    }
}
