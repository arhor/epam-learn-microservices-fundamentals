package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.amazonaws.services.s3.model.AmazonS3Exception
import com.epam.learn.microservices.fundamentals.event.ResourceEvent
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.client.StorageServiceClient
import com.epam.learn.microservices.fundamentals.resource.service.client.StorageType
import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceRepository
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceBinaryDataNotFoundEvent
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEventPublisher
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceDTO
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.lang.invoke.MethodHandles

@Service
@LogExecution
class ResourceServiceImpl(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val resourceDataRepository: ResourceDataRepository,
    private val resourceEventPublisher: ResourceEventPublisher,
    private val resourceRepository: ResourceRepository,
    private val storageServiceClient: StorageServiceClient,
) : ResourceService {

    @Autowired
    private lateinit var circuitBreakerFactory: CircuitBreakerFactory<*, *>

    @Transactional
    override fun saveResource(filename: String, data: ByteArray): Long {
        return saveResourceInternal(
            filename,
            data.inputStream(),
            data.size.toLong(),
        )
    }

    @Transactional
    override fun saveResource(resource: ResourceDTO): Long {
        return saveResourceInternal(
            resource.filename,
            resource.data,
            resource.size,
        )
    }

    override fun getResource(id: Long): ResourceDTO {
        val resource = resourceRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("id = $id")
        val storage = storageServiceClient.getStorageId(resource.storageId)

        val (data, size) = try {
            resourceDataRepository.download(storage.name, resource.filename)
        } catch (e: AmazonS3Exception) {
            applicationEventPublisher.publishEvent(ResourceBinaryDataNotFoundEvent(id))
            throw e
        }

        return ResourceDTO(
            filename = resource.filename,
            data = data,
            size = size,
        )
    }

    @Transactional
    override fun deleteResources(ids: Iterable<Long>): Iterable<Long> {
        val resources = resourceRepository.findAllById(ids)

        val resourcesByStorage = resources
            .map { it.storageId }
            .let { storageServiceClient.getStoragesByIds(it) }
            .associateWith { storage -> resources.filter { it.storageId == storage.id } }

        val deletedFilesByBucket = resourceDataRepository.delete(
            resourcesByStorage
                .map { (storage, resources) -> storage.name to resources.map { it.filename } }
                .toMap()
        )

        val deletedResourceIds = ArrayList<Long>()

        for ((bucket, filenames) in deletedFilesByBucket) {
            deletedResourceIds.addAll(
                resourcesByStorage
                    .filterKeys { it.name == bucket }
                    .values
                    .flatten()
                    .filter { it.filename in filenames }
                    .mapNotNull { it.id }
            )
        }

        if (deletedResourceIds.isNotEmpty()) {
            resourceRepository.deleteAllById(deletedResourceIds)

            resourceEventPublisher.publishEvent(
                ResourceEvent.Deleted(
                    ids = deletedResourceIds
                )
            )
            return deletedResourceIds
        }

        return emptyList()
    }

    private fun saveResourceInternal(filename: String, data: InputStream, size: Long): Long {
        if (resourceRepository.existsByFilename(filename)) {
            throw EntityDuplicateException("filename = $filename")
        }

        val storage = circuitBreakerFactory.create("resource-service").run({
            storageServiceClient.getStorages(
                type = StorageType.STAGING,
                single = true
            ).single()
        }) {
            log.error("Using fallback staging bucket", it)
            mapOf(
                "id" to -1L,
                "name" to "fallback-staging-bucket"
            )
        }

        log.info("Fetched storage data: {}", storage)

        val id: Long by storage
        val name: String by storage

        resourceDataRepository.upload(
            bucket = name,
            filename = filename,
            data = data,
            size = size
        )
        val resource = resourceRepository.save(
            Resource(
                filename = filename,
                storageId = id,
            )
        )
        return resource.id?.also {
            resourceEventPublisher.publishEvent(
                ResourceEvent.Created(
                    id = it
                )
            )
        } ?: throw IllegalStateException("Saved resource must have an ID")
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
