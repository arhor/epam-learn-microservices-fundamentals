package com.epam.learn.microservices.fundamentals.resource.processor.job

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceMetadataProcessor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
@LogExecution
class ScheduledJobs(
    private val songsClient: SongServiceClient,
    private val resourcesClient: ResourceServiceClient,
    private val metadataProcessor: ResourceMetadataProcessor,
) {

    @Scheduled(initialDelay = 5, fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    fun extractAndSaveMetadata() {
        val resourceIds = resourcesClient.fetchUnprocessedResourceIds()

        for (resourceId in resourceIds) {
            if (!songsClient.songMetadataExists(resourceId)) {
                val binaryData = resourcesClient.fetchResourceBinaryData(resourceId)
                val metadata = metadataProcessor.extractMetadata(resourceId, binaryData)

                songsClient.persistMetadata(metadata)
            }
            resourcesClient.updateResourceStatus(resourceId, "COMPLETE")
        }
    }
}
