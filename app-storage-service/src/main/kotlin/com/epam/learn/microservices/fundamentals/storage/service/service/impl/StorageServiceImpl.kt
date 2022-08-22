package com.epam.learn.microservices.fundamentals.storage.service.service.impl

import com.amazonaws.services.s3.AmazonS3
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import com.epam.learn.microservices.fundamentals.storage.service.data.model.Storage
import com.epam.learn.microservices.fundamentals.storage.service.data.repository.StorageRepository
import com.epam.learn.microservices.fundamentals.storage.service.service.StorageService
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageRequestDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageResponseDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.storage.service.service.exception.EntityNotFoundException
import com.epam.learn.microservices.fundamentals.storage.service.service.mapping.StorageMapper
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.invoke.MethodHandles

@Service
@LogExecution
class StorageServiceImpl(
    private val repository: StorageRepository,
    private val mapper: StorageMapper,
    private val amazon: AmazonS3,
) : StorageService {

    @Suppress("KotlinConstantConditions")
    @Transactional(readOnly = true)
    override fun getStorages(type: StorageType?, single: Boolean?): List<StorageResponseDTO> {
        return when {
            (type !== null) && (single === null) -> repository.findAllByType(type)
            (type === null) && (single !== null) -> repository.findAny()
            (type !== null) && (single !== null) -> repository.findSingleByType(type)
            else -> repository.findAll()
        }.map(mapper::mapToDTO)
    }

    @Transactional(readOnly = true)
    override fun getStoragesByIds(ids: List<Long>): List<StorageResponseDTO> {
        return repository.findAllById(ids).map(mapper::mapToDTO)
    }

    @Transactional(readOnly = true)
    override fun getStorageById(storageId: Long): StorageResponseDTO {
        return repository.findByIdOrNull(storageId)?.let(mapper::mapToDTO)
            ?: throw EntityNotFoundException(condition = "id = $storageId")
    }

    @Transactional
    override fun createStorage(dto: StorageRequestDTO): Long {
        val (name, type) = dto
        if (repository.existsByNameAndType(name, type)) {
            throw EntityDuplicateException(condition = "name = $name, type = $type")
        }
        return createStorageInternal(name, type).id
            ?: throw IllegalStateException("Saved storage must have an ID")
    }

    @Transactional
    override fun createDefaultStorages() {
        log.info("Checking if there are missing default storages for available storage types")
        var counter = 0
        for (storageType in StorageType.values()) {
            val storageName = "${defaultStorageName}-${storageType}".lowercase()

            if (!repository.existsByNameAndType(storageName, storageType)) {
                createStorageInternal(storageName, storageType).also {
                    log.info("Created storage - {}", it)
                    counter++
                }
            }
        }
        log.info("Default storages verification complete, created storages count: {}", counter)
    }

    @Transactional
    override fun deleteStorages(storagesIds: Iterable<Long>): Iterable<Long> {
        return repository.findAllById(storagesIds)
            .also(repository::deleteAll)
            .onEach { amazon.deleteBucket(it.name) }
            .mapNotNull { it.id }
    }

    private fun createStorageInternal(name: String, type: StorageType): Storage {
        amazon.createBucket(name)
        return repository.save(Storage(name = name, type = type))
    }

    companion object {
        private const val defaultStorageName = "default-storage"
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
