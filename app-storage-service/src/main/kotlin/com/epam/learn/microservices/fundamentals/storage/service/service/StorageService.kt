package com.epam.learn.microservices.fundamentals.storage.service.service

import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageRequestDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageResponseDTO

interface StorageService {

    fun getStorages(type: StorageType?, single: Boolean?): List<StorageResponseDTO>

    fun getStoragesByIds(ids: List<Long>): List<StorageResponseDTO>

    fun getStorageById(storageId: Long): StorageResponseDTO

    fun createStorage(dto: StorageRequestDTO): Long

    fun createDefaultStorages()

    fun deleteStorages(storagesIds: Iterable<Long>): Iterable<Long>
}
