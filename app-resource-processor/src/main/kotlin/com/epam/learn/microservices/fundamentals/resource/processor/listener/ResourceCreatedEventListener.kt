package com.epam.learn.microservices.fundamentals.resource.processor.listener

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceMetadataProcessor
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
@LogExecution
class ResourceCreatedEventListener(
    private val songsClient: SongServiceClient,
    private val resourcesClient: ResourceServiceClient,
    private val metadataProcessor: ResourceMetadataProcessor,
) {

    @JmsListener(destination = "\${configuration.aws.sqs.queue}")
    fun processCreatedResource(resourceId: Long) {
        val binaryData = resourcesClient.fetchResourceBinaryData(resourceId)
        val metadata = metadataProcessor.extractMetadata(resourceId, binaryData)

        songsClient.persistMetadata(metadata)
    }
}
