package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource
import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource.ProcessingStatus.PENDING
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceRepository
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEvent
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceEventPublisher
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceDTO
import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceUpdateDTO
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.Clock
import java.time.LocalDateTime

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
    override fun getUnprocessedResourceIds(): List<Long> {
        return repository.findUnprocessedResources().let { resources ->
            if (resources.isNotEmpty()) {
                val ids = ArrayList<Long>()
                for (resource in resources) {
                    resource.apply {
                        id?.let(ids::add)
                        status = PENDING
                    }
                }
                repository.saveAll(resources)
                ids
            } else {
                emptyList()
            }
        }
    }

    @Transactional
    override fun resetOutdatedPendingResourcesStatus(): Int {
        val deadline = LocalDateTime.now(Clock.systemUTC()).minusMinutes(10)
        val outdatedResources = repository.findOutdatedPendingResourcesIds(deadline)

        val rowsUpdated = if (outdatedResources.isNotEmpty()) {
            repository.updateResourcesStatus(Resource.ProcessingStatus.NONE, outdatedResources)
        } else {
            0
        }

        return rowsUpdated
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
                        payload = deletedResourceIds
                    )
                )
            }
            deletedResourceIds
        } else {
            emptyList()
        }
    }

    @Transactional
    override fun updateResource(id: Long, dto: ResourceUpdateDTO) {
        val resource = repository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("id = $id")

        resource.status = Resource.ProcessingStatus.valueOf(dto.status)

        repository.save(resource)
    }

    private fun saveResourceInternal(filename: String, data: InputStream, size: Long): Long {
        if (repository.existsByFilename(filename)) {
            throw EntityDuplicateException("filename = $filename")
        }

        dataRepository.upload(filename, data, size)

        val resource = repository.save(
            Resource().also {
                it.filename = filename
                it.status = Resource.ProcessingStatus.NONE
            }
        )
        val resourceId = resource.id ?: throw IllegalStateException("Saved resource must have an ID")

        resourceEventPublisher.publishEvent(
            ResourceEvent.Created(
                payload = resourceId
            )
        )

        return resourceId
    }
}
