package com.epam.learn.microservices.fundamentals.resource.processor.listener

import com.epam.learn.microservices.fundamentals.event.ResourceEvent
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceMetadataProcessor
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
@LogExecution
class ResourceEventListener(
    private val songsClient: SongServiceClient,
    private val resourcesClient: ResourceServiceClient,
    private val metadataProcessor: ResourceMetadataProcessor,
) {

    @JmsListener(destination = "\${configuration.aws.sqs.created-resources-queue}")
    fun processResourceCreatedEvent(event: ResourceEvent.Created) {
        val resourceId = event.id
        val binaryData = resourcesClient.fetchResourceBinaryData(resourceId)
        val metadata = metadataProcessor.extractMetadata(resourceId, binaryData)

        songsClient.createSongMetadata(metadata)
    }

    @JmsListener(destination = "\${configuration.aws.sqs.deleted-resources-queue}")
    fun processResourceDeletedEvent(event: ResourceEvent.Deleted) {
        songsClient.deleteSongsMetadata(event.ids)
    }
}
