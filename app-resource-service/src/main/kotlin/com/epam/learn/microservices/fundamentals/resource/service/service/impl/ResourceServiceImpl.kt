package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceRepository
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEvent
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEventPublisher
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceDTO
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream

@Service
@LogExecution
class ResourceServiceImpl(
    private val repository: ResourceRepository,
    private val dataRepository: ResourceDataRepository,
    private val resourceEventPublisher: ResourceEventPublisher,
) : ResourceService {

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
        val meta = repository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("id = $id")

        val filename = meta.filename
            ?: throw IllegalStateException("filename cannot be null")

        val (data, size) = dataRepository.download(filename)

        return ResourceDTO(
            filename = filename,
            data = data,
            size = size,
        )
    }

    @Transactional
    override fun deleteResources(ids: Iterable<Long>): Iterable<Long> {
        val resourceIdsByFilename =
            repository.findAllById(ids).groupBy(
                Resource::filename,
                Resource::id,
            )

        return if (resourceIdsByFilename.isNotEmpty()) {
            val deletedResourceIds =
                dataRepository.delete(resourceIdsByFilename.keys.filterNotNull())
                    .mapNotNull(resourceIdsByFilename::get)
                    .flatten()
                    .filterNotNull()

            if (deletedResourceIds.isNotEmpty()) {
                repository.deleteAllById(deletedResourceIds)

                resourceEventPublisher.publishEvent(
                    ResourceEvent.Deleted(
                        ids = deletedResourceIds
                    )
                )
            }
            deletedResourceIds
        } else {
            emptyList()
        }
    }

    private fun saveResourceInternal(filename: String, data: InputStream, size: Long): Long {
        if (repository.existsByFilename(filename)) {
            throw EntityDuplicateException("filename = $filename")
        }

        dataRepository.upload(filename, data, size)

        val resource = repository.save(
            Resource().also {
                it.filename = filename
            }
        )
        val resourceId = resource.id ?: throw IllegalStateException("Saved resource must have an ID")

        resourceEventPublisher.publishEvent(
            ResourceEvent.Created(
                id = resourceId
            )
        )

        return resourceId
    }
}
