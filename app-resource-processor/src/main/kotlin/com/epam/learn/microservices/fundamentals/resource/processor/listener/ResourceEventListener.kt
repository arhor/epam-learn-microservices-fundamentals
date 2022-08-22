package com.epam.learn.microservices.fundamentals.resource.processor.listener

import com.epam.learn.microservices.fundamentals.event.ResourceEvent
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceMetadataProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
@LogExecution
class ResourceEventListener(
    private val songsClient: SongServiceClient,
    private val resourcesClient: ResourceServiceClient,
    private val metadataProcessor: ResourceMetadataProcessor,
    private val jmsTemplate: JmsTemplate,
) {

    @Value("\${configuration.aws.sqs.handled-resources-queue}")
    lateinit var handledResourcesQueue: String

    @JmsListener(destination = "resource-created-events")
    fun processResourceCreatedEvent(event: ResourceEvent.Created) {
        val resourceId = event.id
        val binaryData = resourcesClient.fetchResourceBinaryData(resourceId).body?.inputStream
            ?: throw IllegalStateException("Response body cannot be null")
        val metadata = metadataProcessor.extractMetadata(resourceId, binaryData)

        songsClient.createSongMetadata(metadata)

        jmsTemplate.convertAndSend(handledResourcesQueue, ResourceEvent.Handled(resourceId))
    }

    @JmsListener(destination = "resource-deleted-events")
    fun processResourceDeletedEvent(event: ResourceEvent.Deleted) {
        songsClient.deleteSongsMetadata(event.ids)
    }
}
