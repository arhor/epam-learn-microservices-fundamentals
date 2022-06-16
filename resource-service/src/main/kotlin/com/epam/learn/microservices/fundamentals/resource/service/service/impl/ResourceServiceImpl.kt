package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.resource.service.data.model.ResourceMeta
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceMetaRepository
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceDTO
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.resource.service.service.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream

@Service
class ResourceServiceImpl(
    private val metaRepository: ResourceMetaRepository,
    private val dataRepository: ResourceDataRepository,
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

    override fun findResource(id: Long): ResourceDTO {
        val meta = metaRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("id = $id")
        val (data, size) = dataRepository.download(meta.filename)

        return ResourceDTO(
            filename = meta.filename,
            data = data,
            size = size,
        )
    }

    @Transactional
    override fun deleteResources(ids: Iterable<Long>): Iterable<Long> {
        val resourceIdsByFilename =
            metaRepository.findAllById(ids).groupBy(
                ResourceMeta::filename,
                ResourceMeta::id,
            )
        return if (resourceIdsByFilename.isNotEmpty()) {
            val deletedResourceIds =
                dataRepository.delete(resourceIdsByFilename.keys)
                    .mapNotNull(resourceIdsByFilename::get)
                    .flatten()
                    .filterNotNull()

            if (deletedResourceIds.isNotEmpty()) {
                metaRepository.deleteAllById(deletedResourceIds)
            }
            deletedResourceIds
        } else {
            emptyList()
        }
    }

    private fun saveResourceInternal(filename: String, data: InputStream, size: Long): Long {
        if (metaRepository.existsByFilename(filename)) {
            throw EntityDuplicateException("filename = $filename")
        }

        val metadata = dataRepository.upload(filename, data, size)
        val (id) = metaRepository.save(metadata)

        return id ?: throw IllegalStateException("Saved resource must have an ID")
    }
}
