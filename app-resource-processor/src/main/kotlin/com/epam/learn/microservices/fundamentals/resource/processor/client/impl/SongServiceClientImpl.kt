package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@LogExecution
class SongServiceClientImpl(private val restTemplate: RestTemplate) : SongServiceClient {

    @Value("\${configuration.song-service-url}")
    lateinit var songServiceUrl: String

    @Retry(name = "create-song-metadata")
    override fun createSongMetadata(metadata: ResourceMetadata) {
        restTemplate.postForObject(songServiceUrl, metadata, String::class.java)
    }

    @Retry(name = "delete-songs-metadata")
    override fun deleteSongsMetadata(resourceIds: List<Long>) {
        restTemplate.delete("${songServiceUrl}?resources={ids}", resourceIds.joinToString())
    }
}
