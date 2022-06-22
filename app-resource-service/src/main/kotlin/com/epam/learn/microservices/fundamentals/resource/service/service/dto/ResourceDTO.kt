package com.epam.learn.microservices.fundamentals.resource.service.service.dto

import java.io.InputStream

data class ResourceDTO(
    val filename: String,
    val data: InputStream,
    val size: Long,
)
