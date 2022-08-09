package com.epam.learn.microservices.fundamentals.storage.service.service

import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageRequestDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageResponseDTO

interface StorageService {

    fun getStorages(): List<StorageResponseDTO>

    fun getSingleStorageByType(type: StorageType): StorageResponseDTO?

    fun createStorage(dto: StorageRequestDTO): Long

    fun deleteStorages(ids: Iterable<Long>): Iterable<Long>
}
