package com.epam.learn.microservices.fundamentals.storage.service.service.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import com.epam.learn.microservices.fundamentals.storage.service.data.repository.StorageRepository
import com.epam.learn.microservices.fundamentals.storage.service.service.StorageService
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageRequestDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageResponseDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.mapping.StorageMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@LogExecution
@Transactional
class StorageServiceImpl(
    private val repository: StorageRepository,
    private val mapper: StorageMapper,
) : StorageService {

    @Transactional(readOnly = true)
    override fun getStorages(): List<StorageResponseDTO> {
        return repository.findAll().map(mapper::mapToDTO)
    }

    @Transactional(readOnly = true)
    override fun getSingleStorageByType(type: StorageType): StorageResponseDTO? {
        TODO("Not yet implemented")
    }

    override fun createStorage(dto: StorageRequestDTO): Long {
        TODO("Not yet implemented")
    }

    override fun deleteStorages(ids: Iterable<Long>): Iterable<Long> {
        return repository.deleteAllByIdReturningDeletedIds(ids)
    }
}
