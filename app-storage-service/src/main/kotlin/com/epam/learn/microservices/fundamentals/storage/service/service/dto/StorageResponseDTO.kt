package com.epam.learn.microservices.fundamentals.storage.service.service.dto

import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType

data class StorageResponseDTO(
    val id: Long,
    val name: String,
    val type: StorageType,
)
