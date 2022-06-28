package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.invoke.MethodHandles

@Service
@LogExecution
@Retry(name = "song-service-client")
class SongServiceClientImpl(private val http: RestTemplate) : SongServiceClient {

    @Value("\${configuration.song-service-url}")
    lateinit var songServiceUrl: String


    override fun createSongMetadata(metadata: ResourceMetadata) {
        val location = http.postForLocation(songServiceUrl, metadata)
        log.info("Created song metadata, location: {}", location)
    }

    override fun deleteSongsMetadata(resourceIds: List<Long>) {
        http.delete("${songServiceUrl}?resources={ids}", resourceIds)
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
