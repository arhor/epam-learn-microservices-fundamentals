package com.epam.learn.microservices.fundamentals.resource.processor.client

import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata

interface SongServiceClient {

    fun persistMetadata(metadata: ResourceMetadata)
    fun songMetadataExists(resourceId: Long): Boolean
}
