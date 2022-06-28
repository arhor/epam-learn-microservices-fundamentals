package com.epam.learn.microservices.fundamentals.resource.processor.listener

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceEvent
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceEvent.Type.CREATED
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceEvent.Type.DELETED
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceMetadataProcessor
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
    fun processResourceEvent(data: ByteArray) {
        val event = objectMapper.readValue<ResourceEvent>(data)

        when (event.type) {
            CREATED -> {
                val resourceId = event.payload.unwrapResourceId()
                val binaryData = resourcesClient.fetchResourceBinaryData(resourceId)
                val metadata = metadataProcessor.extractMetadata(resourceId, binaryData)

                songsClient.createSongMetadata(metadata)
            }
            DELETED -> {
                val deletedResourceIds = (event.payload as List<*>).map { it.unwrapResourceId() }

                songsClient.deleteSongsMetadata(deletedResourceIds)
            }
        }
    }

    private fun Any?.unwrapResourceId(): Long = when (this) {
        is Number -> this.toLong()
        is String -> this.toLong()
        else -> throw IllegalArgumentException("Resource ID should be represented as Number or String")
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
