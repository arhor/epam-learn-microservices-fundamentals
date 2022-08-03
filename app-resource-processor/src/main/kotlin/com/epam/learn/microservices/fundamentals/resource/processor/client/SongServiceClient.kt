package com.epam.learn.microservices.fundamentals.resource.processor.client

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@LogExecution
@Retry(name = "song-service-client")
@FeignClient(name = "song-service", path = "/api/songs")
interface SongServiceClient {

    @PostMapping
    fun createSongMetadata(metadata: ResourceMetadata)

    @DeleteMapping
    fun deleteSongsMetadata(@RequestParam resources: List<Long>)
}
