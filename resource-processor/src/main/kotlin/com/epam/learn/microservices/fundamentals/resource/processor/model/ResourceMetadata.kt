package com.epam.learn.microservices.fundamentals.resource.processor.model

import java.time.Duration
import java.time.Year

data class ResourceMetadata(
    val year: Year?,
    val name: String?,
    val album: String?,
    val artist: String?,
    val length: Duration,
    val resourceId: Long
)
