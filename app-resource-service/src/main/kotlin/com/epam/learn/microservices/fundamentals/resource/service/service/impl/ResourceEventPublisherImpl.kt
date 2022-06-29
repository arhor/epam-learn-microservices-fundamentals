package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.event.ResourceEvent
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEventPublisher
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.beans.factory.annotation.Value
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
@Retry(name = "resource-event-publisher")
class ResourceEventPublisherImpl(private val jmsTemplate: JmsTemplate) : ResourceEventPublisher {

    @Value("\${configuration.aws.sqs.created-resources-queue}")
    lateinit var createdResourcesQueue: String

    @Value("\${configuration.aws.sqs.deleted-resources-queue}")
    lateinit var deletedResourcesQueue: String

    override fun publishEvent(event: ResourceEvent) {
        jmsTemplate.convertAndSend(
            when (event) {
                is ResourceEvent.Created -> createdResourcesQueue
                is ResourceEvent.Deleted -> deletedResourcesQueue
            },
            event
        )
    }
}
