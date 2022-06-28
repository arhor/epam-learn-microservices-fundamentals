package com.epam.learn.microservices.fundamentals.resource.processor.client

import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata

interface SongServiceClient {

    fun createSongMetadata(metadata: ResourceMetadata)

    fun deleteSongsMetadata(resourceIds: List<Long>)
}
