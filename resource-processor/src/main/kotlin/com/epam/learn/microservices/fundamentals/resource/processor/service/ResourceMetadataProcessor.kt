package com.epam.learn.microservices.fundamentals.resource.processor.service

import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata
import java.io.InputStream

interface ResourceMetadataProcessor {

    fun extractMetadata(data: InputStream): ResourceMetadata
}
