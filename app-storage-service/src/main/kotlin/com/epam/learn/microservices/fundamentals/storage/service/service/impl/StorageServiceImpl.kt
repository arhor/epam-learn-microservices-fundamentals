package com.epam.learn.microservices.fundamentals.storage.service.service.impl

import com.amazonaws.services.s3.AmazonS3
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import com.epam.learn.microservices.fundamentals.storage.service.data.repository.StorageRepository
import com.epam.learn.microservices.fundamentals.storage.service.service.StorageService
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageRequestDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageResponseDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.exception.EntityNotFoundException
import com.epam.learn.microservices.fundamentals.storage.service.service.mapping.StorageMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@LogExecution
@Transactional
class StorageServiceImpl(
    private val repository: StorageRepository,
    private val mapper: StorageMapper,
    private val amazon: AmazonS3,
) : StorageService {

    @Transactional(readOnly = true)
    override fun getStorages(type: StorageType?, single: Boolean?): List<StorageResponseDTO> {
        val storages = when {
            (type !== null) && (single === null) -> repository.findAllByType(type)
            (type === null) && (single !== null) -> repository.findAny()
            (type !== null) && (single !== null) -> repository.findSingleByType(type)
            else -> repository.findAll()
        }
        return storages.map(mapper::mapToDTO)
    }

    @Transactional(readOnly = true)
    override fun getStorageById(storageId: Long): StorageResponseDTO {
        return repository.findByIdOrNull(storageId)?.let(mapper::mapToDTO)
            ?: throw EntityNotFoundException(condition = "id=$storageId")
    }

    override fun createStorage(dto: StorageRequestDTO): Long {
        val storage = mapper.mapToEntity(dto)
        val (id, _, _) = repository.save(storage)

        return id
    }

    override fun deleteStorages(storagesIds: Iterable<Long>): Iterable<Long> {
        return repository.deleteAllByIdReturningDeletedIds(storagesIds)
    }
}
