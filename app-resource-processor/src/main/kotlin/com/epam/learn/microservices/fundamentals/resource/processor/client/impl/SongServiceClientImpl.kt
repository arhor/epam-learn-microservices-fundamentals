package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@LogExecution
class SongServiceClientImpl(private val restTemplate: RestTemplate) : SongServiceClient {

    @Value("\${configuration.song-service-url}")
    lateinit var songServiceUrl: String

    override fun persistMetadata(metadata: ResourceMetadata) {
        restTemplate.postForObject(
            songServiceUrl,
            metadata,
            String::class.java
        )
    }

    override fun songMetadataExists(resourceId: Long): Boolean {
        val result = restTemplate.exchange(
            "$songServiceUrl?resources=$resourceId",
            HttpMethod.GET,
            null,
            ResourceMetadataList
        )
        return result.body?.let { resources -> resources.any { it.resourceId == resourceId } }
            ?: false
    }

    private object ResourceMetadataList : ParameterizedTypeReference<List<ResourceMetadata>>()
}
