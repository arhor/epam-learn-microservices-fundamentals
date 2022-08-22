package com.epam.learn.microservices.fundamentals.resource.service.client

data class StorageResponseDTO(
    val id: Long,
    val name: String,
    val type: StorageType,
)
