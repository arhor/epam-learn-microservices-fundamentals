package com.epam.learn.microservices.fundamentals.resource.processor.model

import java.time.Duration
import java.time.Year

data class ResourceMetadata(
    val name: String?,
    val year: String?,
    val album: String?,
    val artist: String?,
    val length: String?,
    val resourceId: Long
)
